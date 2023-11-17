package com.tms.repository;

import com.tms.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * CardRepository is an interface, that has a connection to <i>cards table
 * performs additional operations associated with the table <i>cards
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * findAllCardNumbers is a method, that finds all card's numbers
     * @return List<String>
     */
    @Query("SELECT c.cardNumber FROM cards c")
    List<String> findAllCardNumbers();

    /**
     * findCardMoneyCurrencyByCardNumber is a method, that finds money currency by card number
     * @return String
     */
    @Query("select moneyCurrency from cards where cardNumber = :cardNumber")
    String findCardMoneyCurrencyByCardNumber(String cardNumber);

    /**
     * findCardTypeByCardNumber is a method, that finds card type by card number
     * @return String
     */
    @Query("select cardType from cards where cardNumber = :cardNumber")
    String findCardTypeByCardNumber(String cardNumber);

    /**
     * findCardByCardNumber is a method, that finds card by card number
     * @return Optional<Card>
     */
    Card findCardByCardNumber(String cardNumber);
}
