package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchBankException extends Exception{
    public NoSuchBankException() {
        log.warn("There's no such bank");
    }
}
