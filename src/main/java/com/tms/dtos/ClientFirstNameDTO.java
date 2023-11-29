package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ClientFirstNameDTO is a DTO class to change client's first name
 */
@Component
@Data
public class ClientFirstNameDTO {
    private String firstName;
}
