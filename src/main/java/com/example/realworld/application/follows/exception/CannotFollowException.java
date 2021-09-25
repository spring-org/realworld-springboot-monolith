package com.example.realworld.application.follows.exception;

public class CannotFollowException extends RuntimeException {
    public CannotFollowException() {
    }

    public CannotFollowException(String message) {
        super(message);
    }
}
