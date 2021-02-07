package com.javasolution.app.bookingservice.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistException extends RuntimeException{

    private String context;
    private String message;

    public AlreadyExistException(final String context, final String message){
        super(String.format("%s: %s", context, message));
        this.context = context;
        this.message = message;
    }
}
