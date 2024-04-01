package com.nagarro.accountmanagement.exceptions;

public class AccountNotFoundException extends  RuntimeException{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
