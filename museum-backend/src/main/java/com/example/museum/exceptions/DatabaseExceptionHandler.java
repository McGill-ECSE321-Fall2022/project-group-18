package com.example.museum.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DatabaseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleEventRegistrationException(DatabaseException ex) {
        return new ResponseEntity<String>(ex.getMessage(), ex.getStatus());
    }
}
