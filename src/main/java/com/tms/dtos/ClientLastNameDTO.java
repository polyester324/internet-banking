package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ClientLastNameDTO is a DTO class to change client's last name
 */
@Component
@Data
public class ClientLastNameDTO {
    private String lastName;
}
