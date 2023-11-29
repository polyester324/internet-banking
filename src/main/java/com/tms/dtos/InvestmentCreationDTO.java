package com.tms.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * InvestmentCreationDTO is a DTO class for investment creation
 */
@Component
@Data
public class InvestmentCreationDTO {
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b")
    private String cardNumber;
    private String bankName;
    private String moneyCurrency;
    private String time;
    @PositiveOrZero
    private BigDecimal amount;
}
