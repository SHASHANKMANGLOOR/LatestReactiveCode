package com.example.joke_app.exception;

public class RestClientException extends RuntimeException {
    public RestClientException(String message) {
        super(message);
    }
}
