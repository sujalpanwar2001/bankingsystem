package com.nagarro.accountmanagement.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling specific exceptions across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountExistsException.class)
    public ResponseEntity<Object> handleCustomerExistsException(AccountExistsException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<Object> handleUserServiceUnavailableException(UserServiceUnavailableException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex) {
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