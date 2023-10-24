package com.tms.repository;

import com.tms.domain.Card;
import com.tms.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Modifying
    @Query("update cards c set c.balance = c.balance + :amount where c.cardNumber = :cardNumber")
    void deposit(String cardNumber, BigDecimal amount);

    @Modifying
    @Query("update cards c set c.balance = c.balance - :amount where c.cardNumber = :cardNumber")
    void withdraw(String cardNumber, BigDecimal amount);

    @Query("select moneyCurrency from cards where cardNumber = :cardNumber")
    String findCardMoneyCurrencyByCardNumber(String cardNumber);

    Optional<Card> findCardByCardNumber(String cardNumber);
}
