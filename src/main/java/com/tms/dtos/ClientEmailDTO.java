package com.tms.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ClientEmailDTO is a DTO class to change client's email
 */
@Component
@Data
public class ClientEmailDTO {
    private String email;
}
