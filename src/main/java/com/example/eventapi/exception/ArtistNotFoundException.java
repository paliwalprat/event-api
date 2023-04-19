package com.example.eventapi.exception;

import org.springframework.http.HttpStatus;

public class ArtistNotFoundException extends RuntimeException  {
    public ArtistNotFoundException(String message) {
        super(message);
    }
}