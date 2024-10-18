package com.example.joke_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> restClientExcepion(RestClientException ex) {
        // Handle RestClientException
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Rest client error: " + ex.getMessage());
    }

    @ExceptionHandler(ServiceExcpetions.class)
    public ResponseEntity<String> serviceUnavailableExcpetion(ServiceExcpetions ex) {
        // Handle ServiceExcpetions
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service error: " + ex.getMessage());
    }
}