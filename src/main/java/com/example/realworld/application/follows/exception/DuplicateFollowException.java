package com.example.realworld.application.follows.exception;

public class DuplicateFollowException extends RuntimeException {
    public DuplicateFollowException() {
    }

    public DuplicateFollowException(String message) {
        super(message);
    }
}
