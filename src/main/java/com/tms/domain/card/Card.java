package com.tms.domain.card;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
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
 * Card is a class, that contains every column of <i>cards table
 */
@Component
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.STRING)
@Entity(name = "cards")
public abstract class Card {
    @Id
    @SequenceGenerator(name = "seq_card", sequenceName = "cards_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_card", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "created")
    private Timestamp created;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "money_currency")
    private String moneyCurrency;
    @Column(name = "card_type", insertable=false, updatable=false)
    private String cardType;
}
