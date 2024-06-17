package com.icarapp.icar.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
