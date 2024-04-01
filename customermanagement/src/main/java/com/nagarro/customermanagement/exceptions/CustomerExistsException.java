package com.nagarro.customermanagement.exceptions;

public class CustomerExistsException extends RuntimeException{


    public CustomerExistsException(String message) {
        super(message);
    }

}
