package com.tms.domain;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import lombok.Data;

@Component
@Data
@Entity(name = "clients")
public class Client {
    @Id
    @SequenceGenerator(name = "seq_client", sequenceName = "clients_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_client", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "created")
    private Timestamp created;
}
