package com.example.realworld.application.follows.exception;

public class AlreadyFollowException extends RuntimeException {
    public AlreadyFollowException(String message) {
        super(message);
    }
}
