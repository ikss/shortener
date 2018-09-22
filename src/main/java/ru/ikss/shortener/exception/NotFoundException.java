package ru.ikss.shortener.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends KnownException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
