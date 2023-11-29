package com.tms.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * CardTransactionDepositAndWithdrawDTO is a DTO class for deposit and withdraw operations
 */
@Component
@Data
public class CardTransactionDepositAndWithdrawDTO {
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b")
    private String card;
    @PositiveOrZero
    private BigDecimal amount;
    private String moneyCurrency;
}
