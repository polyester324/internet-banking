package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CardCreationDTO {
    private String bankName;
    private String moneyCurrency;
    private Long clientId;
}
