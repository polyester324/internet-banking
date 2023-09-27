package com.tms.domain;

import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import lombok.Data;

@Component
@Data
public class Client {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Timestamp created;
}
