package com.tms.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpStatus> exceptionHandlerMethodForRuntimeException(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BankNotFound.class)
    public ResponseEntity<HttpStatus> bankNotFound(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<HttpStatus> cardNotFoundException(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<HttpStatus> checkException(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FileCreationException.class)
    public ResponseEntity<HttpStatus> fileCreationException(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientFromDatabaseNotFound.class)
    public ResponseEntity<HttpStatus> clientFromDatabaseNotFoundException(Exception e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SameClientInDatabaseException.class)
    public ResponseEntity<HttpStatus> sameClientInDatabaseException(Exception e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
