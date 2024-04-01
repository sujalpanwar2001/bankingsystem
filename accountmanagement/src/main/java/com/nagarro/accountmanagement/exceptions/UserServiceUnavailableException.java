package com.nagarro.accountmanagement.exceptions;

public class UserServiceUnavailableException extends  RuntimeException {

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}
