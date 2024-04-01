package com.nagarro.customermanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling specific exceptions across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerExistsException.class)
    public ResponseEntity<Object> handleCustomerExistsException(CustomerExistsException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccountServiceUnavailableException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(AccountServiceUnavailableException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FaultToleranceException.class)
    public ResponseEntity<Object> handleFaultToleranceException(FaultToleranceException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }




}
