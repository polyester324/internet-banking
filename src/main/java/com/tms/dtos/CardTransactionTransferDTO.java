package com.tms.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * CardTransactionTransferDTO is a DTO class for transfer operations
 */
@Component
@Data
public class CardTransactionTransferDTO {
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b")
    private String cardSender;
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b")
    private String cardReceiver;
    @PositiveOrZero
    private BigDecimal amount;
    private String moneyCurrency;
}
