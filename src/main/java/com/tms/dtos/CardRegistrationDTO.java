package com.tms.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Data
public class CardRegistrationDTO {
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b")
    private String cardNumber;
    private Long clientId;
    @PositiveOrZero
    private BigDecimal balance;
    private String moneyCurrency;
    private String cardType;
}
