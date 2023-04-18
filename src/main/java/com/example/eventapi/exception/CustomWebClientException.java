package com.example.eventapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;

public class CustomWebClientException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public CustomWebClientException(String errorMessage, HttpStatus httpStatus) {
        super();
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
