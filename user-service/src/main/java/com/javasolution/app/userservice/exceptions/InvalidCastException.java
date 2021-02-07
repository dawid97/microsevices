package com.javasolution.app.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCastException extends RuntimeException{
    public InvalidCastException(final String message) {
        super(message);
    }
}
