package com.tms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ClientFirstNameDTO is a DTO class to change client's first name
 */
@Component
@Data
public class ClientFirstNameDTO {
    @NotBlank
    @Size(min = 2, max = 26, message = "First name must be between 2 and 26 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;
}
