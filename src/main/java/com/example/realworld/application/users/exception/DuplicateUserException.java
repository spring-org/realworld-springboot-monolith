package com.example.realworld.application.users.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
