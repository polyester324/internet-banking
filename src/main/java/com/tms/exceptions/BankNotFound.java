package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * BankNotFound is an Exception class for the situation when there was a problem with founding bank
 */
@Slf4j
public class BankNotFound extends Exception{
    public BankNotFound() {
        log.warn("Bank not found");
    }
}
