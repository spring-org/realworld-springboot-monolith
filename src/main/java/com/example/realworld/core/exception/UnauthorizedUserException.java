package com.example.realworld.core.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }
}
