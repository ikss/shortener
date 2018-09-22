package ru.ikss.shortener.model;

import java.time.LocalDateTime;

public class ErrorResponse implements BaseResponse {
    private final String message;
    private final String error;
    private final LocalDateTime timestamp;
    private final String path;

    public ErrorResponse(String message, String error, String path) {
        this.message = message;
        this.error = error;
        this.path = path;
        timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
}
