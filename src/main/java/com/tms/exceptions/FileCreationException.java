package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * FileCreationException is an Exception class for the situation when there was a problem with file creation
 */
@Slf4j
public class FileCreationException extends Exception{
    public FileCreationException() {
        log.warn("File creation has failed");
    }
}
