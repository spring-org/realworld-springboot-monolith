package com.example.realworld.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }
}
