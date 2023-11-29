package com.tms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ClientPasswordDTO is a DTO class to change client's password
 */
@Component
@Data
public class ClientPasswordDTO {
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,26}$", message = "Password must be 8-26 characters long and contain at least one uppercase letter and one digit")
    private String password;
}

