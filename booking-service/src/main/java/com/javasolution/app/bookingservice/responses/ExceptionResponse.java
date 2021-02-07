package com.javasolution.app.bookingservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ExceptionResponse {

    private String context;
    private String message;
}
