package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * InvestmentCreationDTO is a DTO class for investment creation
 */
@Component
@Data
public class InvestmentCreationDTO {
    private String cardNumber;
    private String bankName;
    private String moneyCurrency;
    private String time;
    private BigDecimal amount;
}
