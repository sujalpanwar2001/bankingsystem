package com.nagarro.accountmanagement.exceptions;

public class AccountExistsException extends RuntimeException{


    public AccountExistsException(String message) {
        super(message);
    }

}
