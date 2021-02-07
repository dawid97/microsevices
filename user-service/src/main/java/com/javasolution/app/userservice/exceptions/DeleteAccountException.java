package com.javasolution.app.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeleteAccountException extends RuntimeException{
    public DeleteAccountException(final String message) {
        super(message);
    }
}
