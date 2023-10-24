package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * CardTransactionDepositAndWithdrawDTO is a DTO class for deposit and withdraw operations
 */
@Component
@Data
public class CardTransactionDepositAndWithdrawDTO {
    private String card;
    private BigDecimal amount;
    private String moneyCurrency;
}
