package com.example.museum.exceptions;

import org.springframework.http.HttpStatus;


@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException{
    private HttpStatus status;

    public DatabaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
