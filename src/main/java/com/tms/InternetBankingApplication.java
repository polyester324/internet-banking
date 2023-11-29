package com.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * InternetBankingApplication is a main class
 */
@SpringBootApplication
@EnableScheduling
public class InternetBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternetBankingApplication.class, args);
    }
}
