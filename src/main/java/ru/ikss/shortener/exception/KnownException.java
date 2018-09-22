package ru.ikss.shortener.exception;

import org.springframework.http.HttpStatus;

public class KnownException extends RuntimeException {
    private final HttpStatus httpStatus;

    public KnownException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
