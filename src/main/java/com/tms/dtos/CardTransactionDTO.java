package com.tms.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CardTransactionDTO {
    private String cardSender;
    private String cardReceiver;
    private BigDecimal amount;
}
