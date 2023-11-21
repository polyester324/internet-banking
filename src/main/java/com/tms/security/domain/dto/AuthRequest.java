package com.tms.security.domain.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}