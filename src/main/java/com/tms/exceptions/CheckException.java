package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckException extends Exception{
    public CheckException() {
        log.warn("Check creation has failed");
    }
}
