package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * CardNotFoundException is an Exception class for the situation when there was a problem with founding card
 */
@Slf4j
public class CardNotFoundException extends Exception{
    public CardNotFoundException() {
        log.warn("Card was not found");
    }
}
