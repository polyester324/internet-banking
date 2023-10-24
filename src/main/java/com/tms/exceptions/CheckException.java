package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * CheckException is an Exception class for the situation when there was a problem with check creation
 */
@Slf4j
public class CheckException extends Exception{
    public CheckException() {
        log.warn("Check creation has failed");
    }
}
