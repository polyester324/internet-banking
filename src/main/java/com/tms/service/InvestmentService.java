package com.tms.service;

import com.tms.domain.Investment;
import com.tms.domain.InvestmentTime;
import com.tms.domain.bank.Bank;
import com.tms.domain.card.Card;
import com.tms.exceptions.NoAccessByIdException;
import com.tms.repository.InvestmentRepository;
import com.tms.security.service.SecurityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@RequiredArgsConstructor
@Service
@Data
public class InvestmentService {
    private final BankService bankService;
    private final CardService cardService;
    private final TransactionService transactionService;
    private final InvestmentRepository investmentRepository;
    private final SecurityService securityService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Integer LOWEST_NUMBER_FOR_INVESTMENT = 100_000_000;
    private final Integer HIGHEST_NUMBER_FOR_INVESTMENT = 900_000_000;

    public List<String> getAllInvestmentNumbers() {
        return investmentRepository.findAllInvestmentNumbers();
    }

    public List<Investment> getAll() {
        return investmentRepository.findAll();
    }

    public Optional<Investment> getInvestmentById(Long id){
        if (securityService.checkAccessById(id)) {
            return investmentRepository.findById(id);
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method createInvestment adds investment data to db
     * @return true if investment was created and false otherwise
     */
    public Boolean createInvestment(String cardNumber, String bankName, String moneyCurrency, String time, BigDecimal amount, Long clientId){
        try {
            if (securityService.checkAccessById(clientId) && cardService.getCardByCardNumber(cardNumber).getClientId().equals(clientId)) {
                Bank bank = bankService.getBankByBankName(bankName);
                BigDecimal commission = bank.getCommission().divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
                System.out.println(commission);
                Investment investment = new Investment();
                investment.setBankId(bank.getId());
                Card card = cardService.getCardByCardNumber(cardNumber);
                log.info(transactionService.withdraw(cardNumber, amount, moneyCurrency));
                investment.setCardId(card.getId());
                investment.setInvestedAmount(amount);
                investment.setTime(time);
                List<String> investmentNumbers = getAllInvestmentNumbers();
                String investmentNumber;
                do {
                    investmentNumber = String.valueOf(new Random().nextInt(HIGHEST_NUMBER_FOR_INVESTMENT) + LOWEST_NUMBER_FOR_INVESTMENT);
                } while (investmentNumbers.contains(investmentNumber));
                investment.setInvestmentNumber(investmentNumber);
                investment.setMoneyCurrency(moneyCurrency);
                LocalDateTime created = LocalDateTime.now();
                investment.setCreated(Timestamp.valueOf(created));
                amount = amount.add(amount.multiply(InvestmentTime.valueOf(time).getCOEFFICIENT().subtract(commission)));
                investment.setExpired(Timestamp.valueOf(created.plusMonths(InvestmentTime.valueOf(time).getMONTHS_AMOUNT())));
                investment.setExpectedAmount(amount);
                investment.setClientId(clientId);
                investmentRepository.save(investment);
                log.info(String.format("investment for card with number %s has been created.", cardNumber));
                return true;
            }
        } catch (Exception e){
            log.info(String.format("investment for card with number %s has not been created. Exception: %s", cardNumber, e));
            return false;
        }
        throw new NoAccessByIdException(clientId, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Boolean updateInvestment(Investment investment) {
        try {
            investmentRepository.saveAndFlush(investment);
            log.info(String.format("investment with id %s was updated", investment.getId()));
        } catch (Exception e){
            log.warn(String.format("have problem updating investment with id %s have error %s", investment.getId(), e));
            return false;
        }
        return true;
    }

    public Boolean deleteInvestmentById(Long id){
        try {
            investmentRepository.deleteById(id);
            log.info(String.format("investment with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting investment with id %s have error %s", id, e));
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 60*1000)
    public void deleteAndAccrueExpiredInvestments() {
        log.info("method deleteAndAccrueExpiredInvestments is working...");
        try {
            List<Investment> investments = getAll();
            for (Investment investment : investments) {
                if (investment.getExpired().before(Timestamp.valueOf(LocalDateTime.now()))) {
                    Card card = cardService.getCardById(investment.getCardId());
                    log.info(String.format("investment accrual with id: %s was successful", investment.getId()));
                    log.info(transactionService.deposit(card.getCardNumber(), investment.getExpectedAmount(), investment.getMoneyCurrency()));
                    deleteInvestmentById(investment.getId());
                    log.info(String.format("investment with id %s was accrued and deleted", investment.getId()));
                }
            }
        } catch (Exception e){
            log.warn(String.format("deleteAndAccrueExpiredInvestments went wrong: %s", e));
        }
    }
}
