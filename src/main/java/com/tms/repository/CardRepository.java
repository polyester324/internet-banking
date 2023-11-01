package com.tms.repository;

import com.tms.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

/**
 * CardRepository is an interface, that has a connection to <i>cards table
 * performs additional operations associated with the table <i>cards
 */

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c.cardNumber FROM cards c")
    List<String> findAllCardNumbers();
    /**
     * deposit is a method, that replenishes the current balance with a specified amount
     */
    @Modifying
    @Query("update cards c set c.balance = c.balance + :amount where c.cardNumber = :cardNumber")
    void deposit(String cardNumber, BigDecimal amount);

    /**
     * withdraw is a method, that removes a specified amount from the current balance
     */
    @Modifying
    @Query("update cards c set c.balance = c.balance - :amount where c.cardNumber = :cardNumber")
    void withdraw(String cardNumber, BigDecimal amount);

    /**
     * findCardMoneyCurrencyByCardNumber is a method, that finds money currency by card number
     * @return String
     */
    @Query("select moneyCurrency from cards where cardNumber = :cardNumber")
    String findCardMoneyCurrencyByCardNumber(String cardNumber);

    @Query("select cardType from cards where cardNumber = :cardNumber")
    String findCardTypeByCardNumber(String cardNumber);

    /**
     * findCardByCardNumber is a method, that finds card by card number
     * @return Optional<Card>
     */
    Card findCardByCardNumber(String cardNumber);
}
