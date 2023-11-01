package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardNotFoundException extends Exception{
    public CardNotFoundException() {
        log.warn("Check creation has failed");
    }
}
