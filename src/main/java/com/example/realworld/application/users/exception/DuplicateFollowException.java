package com.example.realworld.application.users.exception;

public class DuplicateFollowException extends RuntimeException {
    public DuplicateFollowException() {
    }

    public DuplicateFollowException(String message) {
        super(message);
    }
}
