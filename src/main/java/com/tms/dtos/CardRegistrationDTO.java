package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * CardRegistrationDTO is a DTO class for card registration
 */
@Component
@Data
public class CardRegistrationDTO {
    private Long id;
    private String cardNumber;
    private Long clientId;
    private Timestamp created;
    private BigDecimal balance;
    private String moneyCurrency;
    private String cardType;
}
