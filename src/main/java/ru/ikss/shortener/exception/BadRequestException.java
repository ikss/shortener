package ru.ikss.shortener.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends KnownException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
