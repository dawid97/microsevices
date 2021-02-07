package com.javasolution.app.bookingservice.exceptions;

import com.javasolution.app.bookingservice.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleSlotsAlreadyExist(final AlreadyExistException ex, final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getContext(),ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleNotFoundException(final NotFoundException ex, final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getContext(),ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}