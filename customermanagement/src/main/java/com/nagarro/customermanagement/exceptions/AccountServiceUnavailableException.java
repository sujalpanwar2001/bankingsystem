package com.nagarro.customermanagement.exceptions;

public class AccountServiceUnavailableException extends  RuntimeException{
    public AccountServiceUnavailableException(String message) {
        super(message);
    }

}
