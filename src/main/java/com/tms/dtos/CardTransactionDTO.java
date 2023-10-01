package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Data
public class CardTransactionDTO {
    private String cardSender;
    private String cardReceiver;
    private BigDecimal amount;
    private String moneyCurrency;
}
