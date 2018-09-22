package ru.ikss.shortener.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.ikss.shortener.model.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(KnownException.class)
    public ResponseEntity knownExceptionHandler(HttpServletRequest request, KnownException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), exception.getStatus().getReasonPhrase(), request.getRequestURI()), exception.getStatus());
    }
}
