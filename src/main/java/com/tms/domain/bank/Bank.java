package com.tms.domain.bank;


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

@Component
@Data
@Entity(name = "banks")
public abstract class Bank {
    @Id
    @SequenceGenerator(name = "seq_banks", sequenceName = "banks_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_banks", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "commission")
    private BigDecimal commission;
    @Column(name = "created")
    private Timestamp created;
}
