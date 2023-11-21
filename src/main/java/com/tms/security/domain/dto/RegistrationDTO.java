package com.tms.security.domain.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}