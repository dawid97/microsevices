package com.javasolution.app.resourceservice.exceptions;

import com.javasolution.app.resourceservice.responses.ExceptionResponse;
import com.javasolution.app.resourceservice.responses.SlotsAlreadyExistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleNotFoundException(final ExistsException ex, final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getContext(),ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleDivisionException(final DivisionException ex, final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getContext(),ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleSlotsAlreadyExist(final SlotsAlreadyExistException ex, final WebRequest request) {
        final SlotsAlreadyExistResponse exceptionResponse = new SlotsAlreadyExistResponse(ex.getMessage(), Collections.singletonList(ex.getCollisionSlots()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}