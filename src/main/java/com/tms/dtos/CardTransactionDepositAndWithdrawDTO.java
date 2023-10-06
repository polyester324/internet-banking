package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Data
public class CardTransactionDepositAndWithdrawDTO {
    private String card;
    private BigDecimal amount;
    private String moneyCurrency;
}
