package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankNotFound extends Exception{
    public BankNotFound() {
        log.warn("Bank not found");
    }
}
