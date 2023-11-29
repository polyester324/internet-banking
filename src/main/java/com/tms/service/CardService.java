package com.tms.service;

import com.tms.domain.Client;
import com.tms.domain.Investment;
import com.tms.domain.bank.BankFactory;
import com.tms.domain.card.Card;
import com.tms.exceptions.CardNotFoundException;
import com.tms.repository.BankRepository;
import com.tms.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CardService is a class, that has a connection to <i>CardRepository
 * performs crud and other operations associated with the table <i>cards
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final BankRepository bankRepository;

    /**
     * Method getCardById gets Card by requested id
     * @return Optional<Card>
     */
    public Card getCardById(Long id) {
        if (cardRepository.findById(id).isPresent()) {
            return cardRepository.findById(id).get();
        }
        throw new CardNotFoundException();
    }

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    /**
     * Method registerCard adds already existing card data to db
     * @return true if card was registered and false otherwise
     */
    public Boolean registerCard(String cardNumber, Long ClientId, BigDecimal balance, String bankName, String moneyCurrency){
        BankFactory bank = (BankFactory) bankRepository.findBankByBankName(bankName);
        Card card = bank.createCard();
        try {
            card.setCardNumber(cardNumber);
            card.setClientId(ClientId);
            card.setBalance(balance);
            card.setMoneyCurrency(moneyCurrency);
            card.setCardType(bankName);
            card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            cardRepository.save(card);
            log.info(String.format("card with card number %s was registered", card.getCardNumber()));
        } catch (Exception e){
            log.warn(String.format("have problem registering card with card number %s have error %s", card.getCardNumber(), e));
            return false;
        }
        return true;
    }

    /**
     * Method getCardByNumber gets Card by requested card number
     * @return Card
     */
    public Card getCardByCardNumber(String cardNumber) {
        return cardRepository.findCardByCardNumber(cardNumber);
    }

    /**
     * Method updateCard updates card from json data to db
     * @return true if card was updated and false otherwise
     */
    public Boolean updateCard(Card card) {
        try {
            cardRepository.saveAndFlush(card);
            log.info(String.format("card with id %s was updated", card.getId()));
        } catch (Exception e){
            log.warn(String.format("have problem updating card with id %s have error %s", card.getId(), e));
            return false;
        }
        return true;
    }

    /**
     * Method deleteCardById deletes card from db by id
     * @return true if card was deleted and false otherwise
     */
    public boolean deleteCardById(Long id){
        try {
            cardRepository.deleteById(id);
            log.info(String.format("card with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting card with id %s have error %s", id, e));
            return false;
        }
        return true;
    }
}
