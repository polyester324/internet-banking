package com.tms.service;

import com.tms.domain.bank.BankFactory;
import com.tms.domain.card.Card;
import com.tms.repository.BankFactoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class BankFactoryService {
    private final BankFactoryRepository bankFactoryRepository;
    private final BankService bankService;
    private final CardService cardService;

    public Boolean createCard(String bankName, String moneyCurrency, Long ClientId){
        try {
            BankFactory bank = (BankFactory) bankService.getBankByName(bankName);
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
