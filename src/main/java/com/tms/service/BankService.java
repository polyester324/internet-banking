package com.tms.service;

import com.tms.domain.bank.Bank;
import com.tms.domain.bank.BankFactory;
import com.tms.domain.card.Card;
import com.tms.exceptions.BankNotFound;
import com.tms.exceptions.NoAccessByIdException;
import com.tms.repository.BankRepository;
import com.tms.repository.CardRepository;
import com.tms.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankService {
    private final BankRepository bankRepository;
    private final CardRepository cardRepository;
    private final SecurityService securityService;

    public Bank getBankById(Long id){
        return bankRepository.findById(id).orElseThrow(BankNotFound::new);
    }

    public Bank getBankByBankName(String bankName){
        return bankRepository.findBankByBankName(bankName);
    }

    public List<Bank> getAll() {
        return bankRepository.findAll();
    }

    public Boolean updateBank(Long id, String bankName, BigDecimal commission, Timestamp created) {
        try {
            Bank bank = bankRepository.findBankByBankName(bankName);
            bank.setId(id);
            bank.setBankName(bankName);
            bank.setCommission(commission);
            bank.setCreated(created);
            bank.setId(id);
            bankRepository.saveAndFlush(bank);
            log.info(String.format("bank with id %s was updated", bank.getId()));
        } catch (Exception e){
            log.warn(String.format("have problem updating bank with id %s have error %s", id, e));
            return false;
        }
        return true;
    }

    public Boolean deleteBankById(Long id) {
        try {
            bankRepository.deleteById(id);
            log.info(String.format("bank with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting bank with id %s have error %s", id, e));
            return false;
        }
        return true;
    }

    public Boolean createCard(String bankName, String moneyCurrency, Long clientId){
        if (securityService.checkAccessById(clientId)) {
            try {
                BankFactory bank = (BankFactory) bankRepository.findBankByBankName(bankName);
                Card card = bank.createCard();
                List<String> cardNumbers = cardRepository.findAllCardNumbers();
                String cardNumber;
                do {
                    cardNumber = (new Random().nextInt(9000) + 1000)
                            + "-" + (new Random().nextInt(9000) + 1000)
                            + "-" + (new Random().nextInt(9000) + 1000)
                            + "-" + (new Random().nextInt(9000) + 1000);
                } while (cardNumbers.contains(cardNumber));
                card.setCardNumber(cardNumber);
                card.setClientId(clientId);
                card.setBalance(BigDecimal.valueOf(0));
                card.setMoneyCurrency(moneyCurrency);
                card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
                cardRepository.save(card);
                return true;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return false;
        }
        throw new NoAccessByIdException(clientId, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
