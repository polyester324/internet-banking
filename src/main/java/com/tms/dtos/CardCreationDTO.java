package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * CardCreationDTO is a DTO class for bank card creation
 */
@Component
@Data
public class CardCreationDTO {
    private String bankName;
    private String moneyCurrency;
}
