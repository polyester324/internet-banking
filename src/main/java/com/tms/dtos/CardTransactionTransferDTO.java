package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * CardTransactionTransferDTO is a DTO class for transfer operations
 */
@Component
@Data
public class CardTransactionTransferDTO {
    private String cardSender;
    private String cardReceiver;
    private BigDecimal amount;
    private String moneyCurrency;
}
