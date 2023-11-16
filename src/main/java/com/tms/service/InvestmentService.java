package com.tms.service;

import com.tms.domain.Investment;
import com.tms.domain.bank.Bank;
import com.tms.domain.card.Card;
import com.tms.repository.InvestmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
@Data
public class InvestmentService {
    private final BankService bankService;
    private final CardService cardService;
    private final InvestmentRepository investmentRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public List<String> getAllInvestmentNumbers() {
        return investmentRepository.findAllInvestmentNumbers();
    }

    public List<Investment> getAll() {
        return investmentRepository.findAll();
    }

    public Optional<Investment> getInvestmentById(Long id){
        return investmentRepository.findById(id);
    }

    /**
     * Method createInvestment adds investment data to db
     * @return true if investment was created and false otherwise
     */
    public Boolean createInvestment(String cardNumber, String bankName, String moneyCurrency, Double time, BigDecimal amount){
        try {
            Bank bank = bankService.getBankByBankName(bankName);
            BigDecimal commission = bank.getCommission().divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
            System.out.println(commission);
            Investment investment = new Investment();
            investment.setBankId(bank.getId());
            Card card = cardService.getCardByCardNumber(cardNumber);
            log.info(cardService.withdraw(cardNumber, amount, moneyCurrency));
            investment.setCardId(card.getId());
            investment.setInvestedAmount(amount);
            investment.setTime(time);
            List<String> investmentNumbers = getAllInvestmentNumbers();
            String investmentNumber;
            do {
                investmentNumber = String.valueOf(new Random().nextInt(900_000_000) + 100_000_000);
            } while (investmentNumbers.contains(investmentNumber));
            investment.setInvestmentNumber(investmentNumber);
            investment.setMoneyCurrency(moneyCurrency);
            LocalDateTime created = LocalDateTime.now();
            investment.setCreated(Timestamp.valueOf(created));
            if (time == 0.5) {
                amount = amount.add(amount.multiply(BigDecimal.valueOf(0.04).subtract(commission)));
                investment.setExpired(Timestamp.valueOf(created.plusMonths(6)));
                investment.setExpectedAmount(amount);
            } else if (time == 1) {
                amount = amount.add(amount.multiply(BigDecimal.valueOf(0.1).subtract(commission)));
                investment.setExpired(Timestamp.valueOf(created.plusYears(1)));
                investment.setExpectedAmount(amount);
            } else if (time == 2) {
                amount = amount.add(amount.multiply(BigDecimal.valueOf(0.23).subtract(commission)));
                investment.setExpired(Timestamp.valueOf(created.plusYears(2)));
                investment.setExpectedAmount(amount);
            }
            investmentRepository.save(investment);
        } catch (Exception e){
            log.info(String.format("investment for card with number %s has not been created. Exception: %s", cardNumber, e));
            return false;
        }
        log.info(String.format("investment for card with number %s has been created.", cardNumber));
        return true;
    }

    public void deleteInvestmentById(Long id){
        try {
            investmentRepository.deleteById(id);
            log.info(String.format("investment with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting investment with id %s have error %s", id, e));
        }
    }

    public void accrueInvestments(Long investmentId) {
        Investment investment = new Investment();
        if (getInvestmentById(investmentId).isPresent()){
            investment = getInvestmentById(investmentId).get();
        }
        Card card = cardService.getCardById(investment.getCardId());
        log.info(String.format("investment accrual with id: %s was successful", investmentId));
        log.info(cardService.deposit(card.getCardNumber(), investment.getExpectedAmount(), investment.getMoneyCurrency()));
    }

    @PostConstruct
    public String startMonthlyInvestmentCheck() {
        scheduler.scheduleAtFixedRate(qwerty(), 0, 1, TimeUnit.MINUTES);
        return "method startMonthlyInterestPayment starts...";
    }

    public void deleteAndAccrueExpiredInvestments() {
        try {
            List<Investment> investments = getAll();
            for (Investment investment : investments) {
                if (investment.getExpired().before(Timestamp.valueOf(LocalDateTime.now()))) {
                    accrueInvestments(investment.getId());
                    deleteInvestmentById(investment.getId());
                    log.info(String.format("investment with id %s was accrued and deleted", investment.getId()));
                }
            }
        } catch (Exception e){
            log.warn(String.format("deleteAndAccrueExpiredInvestments went wrong: %s", e));
        }
    }

    public Runnable qwerty(){
        return this::deleteAndAccrueExpiredInvestments;
    }
}
