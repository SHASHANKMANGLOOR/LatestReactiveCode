package com.example.joke_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.joke_app.errorConstants.ErrorConstants.SERVICE_UNAVAILABLE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceExcpetions.class)
    public ResponseEntity<String> serviceUnavailableExcpetion(ServiceExcpetions ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                SERVICE_UNAVAILABLE + ex.getMessage()
        );
    }

}