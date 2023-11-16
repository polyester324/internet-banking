package com.tms.service;

import com.tms.domain.bank.Bank;
import com.tms.domain.bank.BankFactory;
import com.tms.domain.card.Card;
import com.tms.exceptions.BankNotFound;
import com.tms.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CardService cardService;

    public Bank getBankByBankName(String bankName) {
        return bankRepository.findBankByBankName(bankName);
    }

    public Bank getBankById(Long id) throws BankNotFound {
        if (bankRepository.findById(id).isPresent()) {
            return bankRepository.findById(id).get();
        }
        throw new BankNotFound();
    }

    public List<Bank> getAll() {
        return bankRepository.findAll();
    }

    /**
     * Method createBank adds bank data to db
     * @return true bank card was created and false otherwise
     */
    public Boolean createBank(Bank bank) {
        try {
            bank.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            bankRepository.save(bank);
            log.info(String.format("bank with name %s was created", bank.getBankName()));
        } catch (Exception e){
            log.warn(String.format("have problem creating bank with name %s have error %s", bank.getBankName(), e));
            return false;
        }
        return true;
    }

    public Boolean updateBank(Bank bank) {
        try {
            bankRepository.saveAndFlush(bank);
            log.info(String.format("bank with id %s was updated", bank.getId()));
        } catch (Exception e){
            log.warn(String.format("have problem updating bank with id %s have error %s", bank.getId(), e));
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

    public Boolean createCard(String bankName, String moneyCurrency, Long ClientId){
        try {
            BankFactory bank = (BankFactory) getBankByBankName(bankName);
            Card card = bank.createCard();
            List<String> cardNumbers = cardService.getAllCardNumbers();
            String cardNumber;
            do {
                cardNumber = (new Random().nextInt(9000) + 1000)
                        + "-" + (new Random().nextInt(9000) + 1000)
                        + "-" + (new Random().nextInt(9000) + 1000)
                        + "-" + (new Random().nextInt(9000) + 1000);
            } while (cardNumbers.contains(cardNumber));
            card.setCardNumber(cardNumber);
            card.setClientId(ClientId);
            card.setBalance(BigDecimal.valueOf(0));
            card.setMoneyCurrency(moneyCurrency);
            card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            cardService.createCard(card);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
