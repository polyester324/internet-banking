package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileCreationException extends Exception{
    public FileCreationException() {
        log.warn("File creation has failed");
    }
}
