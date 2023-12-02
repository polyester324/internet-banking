package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * BankUpdateDTO is a DTO class for bank update
 */
@Component
@Data
public class BankUpdateDTO{
    private Long id;
    private String bankName;
    private BigDecimal commission;
    private Timestamp created;
}
