package com.javasolution.app.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableSendEmailException extends RuntimeException {
    public UnableSendEmailException(final String message) {
        super(message);
    }
}
