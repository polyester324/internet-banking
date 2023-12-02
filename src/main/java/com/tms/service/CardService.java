package com.tms.service;

import com.tms.domain.bank.BankFactory;
import com.tms.domain.card.Card;
import com.tms.exceptions.CardNotFoundException;
import com.tms.exceptions.NoAccessByIdException;
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

/**
 * CardService is a class, that has a connection to <i>CardRepository
 * performs crud and other operations associated with the table <i>cards
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final BankService bankService;
    private final SecurityService securityService;

    /**
     * Method getCardById gets Card by requested id
     * @return Optional<Card>
     */
    public Card getCardById(Long id) {
        if (securityService.checkAccessById(id)) {
            return cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    /**
     * Method createCard adds card from json data to db
     * @return true if card was created and false otherwise
     */
    public Boolean createCard(String cardNumber, Long clientId, BigDecimal balance, String moneyCurrency, String cardType) {
        try {
            BankFactory bank = (BankFactory) bankService.getBankByBankName(cardType);
            Card card = bank.createCard();
            card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            card.setCardNumber(cardNumber);
            card.setClientId(clientId);
            card.setBalance(balance);
            card.setMoneyCurrency(moneyCurrency);
            card.setCardType(cardType);
            cardRepository.save(card);
            log.info(String.format("card with card number %s was created", cardNumber));
        } catch (Exception e){
            log.warn(String.format("have problem creating card with card number %s have error %s", cardNumber, e));
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
    public Boolean updateCard(Long id, String cardNumber, Long clientId, BigDecimal balance, String moneyCurrency, String cardType) {
        try {
            BankFactory bank = (BankFactory) bankService.getBankByBankName(cardType);
            Card card = bank.createCard();
            card.setId(id);
            card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            card.setCardNumber(cardNumber);
            card.setClientId(clientId);
            card.setBalance(balance);
            card.setMoneyCurrency(moneyCurrency);
            card.setCardType(cardType);
            cardRepository.saveAndFlush(card);
            log.info(String.format("card with id %s was updated", id));
        } catch (Exception e){
            log.warn(String.format("have problem updating card with id %s have error %s", id, e));
            return false;
        }
        return true;
    }

    /**
     * Method deleteCardById deletes card from db by id
     * @return true if card was deleted and false otherwise
     */
    public boolean deleteCardById(Long id){
        if (securityService.checkAccessById(id)) {
            try {
                cardRepository.deleteById(id);
                log.info(String.format("card with id %s was deleted", id));
            } catch (Exception e){
                log.warn(String.format("have problem deleting card with id %s have error %s", id, e));
                return false;
            }
            return true;
        }
        throw new NoAccessByIdException(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
