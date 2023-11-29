package com.tms.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

/**
 * Investment is a class, that contains every column of <i>investments table
 */
@Component
@Data
@Entity(name = "investments")
public class Investment {
    @Id
    @SequenceGenerator(name = "seq_investment", sequenceName = "investments_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_investment", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "investment_number")
    private String investmentNumber;
    @Column(name = "card_id")
    private Long cardId;
    @Column(name = "bank_id")
    private Long bankId;
    @Column(name = "money_currency")
    private String moneyCurrency;
    @Column(name = "created")
    private Timestamp created;
    @Column(name = "expired")
    private Timestamp expired;
    @Column(name = "invested_amount")
    private BigDecimal investedAmount;
    @Column(name = "expected_amount")
    private BigDecimal expectedAmount;
    @Column(name = "time")
    private String time;
    @Column(name = "client_id")
    private Long clientId;
}
