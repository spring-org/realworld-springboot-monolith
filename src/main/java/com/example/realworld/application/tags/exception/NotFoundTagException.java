package com.example.realworld.application.tags.exception;

public class NotFoundTagException extends RuntimeException {
    public NotFoundTagException() {
    }

    public NotFoundTagException(String message) {
        super(message);
    }
}
