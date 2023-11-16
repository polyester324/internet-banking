package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Data
public class CardRegistrationDTO {
    private String cardNumber;
    private Long clientId;
    private BigDecimal balance;
    private String moneyCurrency;
    private String cardType;
}
