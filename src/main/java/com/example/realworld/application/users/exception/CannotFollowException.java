package com.example.realworld.application.users.exception;

public class CannotFollowException extends RuntimeException {
    public CannotFollowException() {
    }

    public CannotFollowException(String message) {
        super(message);
    }
}
