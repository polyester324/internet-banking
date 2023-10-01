package com.tms.domain;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

@Component
@Data
public class Card {
    private Long id;
    private String cardNumber;
    private Long clientId;
    private Timestamp created;
    private BigDecimal balance;
    private String moneyCurrency;
}
