package com.tms.domain.bank;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Bank is a class, that contains every column of <i>banks table
 */
@Component
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "bank_name", discriminatorType = DiscriminatorType.STRING)
@Entity(name = "banks")
public abstract class Bank {
    @Id
    @SequenceGenerator(name = "seq_banks", sequenceName = "banks_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_banks", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "bank_name", insertable=false, updatable=false)
    private String bankName;
    @Column(name = "commission")
    private BigDecimal commission;
    @Column(name = "created")
    private Timestamp created;
}
