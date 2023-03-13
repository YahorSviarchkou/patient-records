package com.patient.records.controller.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(Exception ex, WebRequest webRequest) {
        ErrorDetails errorDetails = getErrorDetails(ex, webRequest);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Throwable ex, WebRequest webRequest) {
        ErrorDetails errorDetails = getErrorDetails(ex, webRequest);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    private ErrorDetails getErrorDetails(Throwable ex, WebRequest webRequest) {
        return new ErrorDetails(LocalDateTime.now(), ExceptionUtils.getRootCauseMessage(ex), webRequest.getDescription(false));
    }
}
