package com.tms.repository;

import com.tms.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Card, Long> {
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
}
