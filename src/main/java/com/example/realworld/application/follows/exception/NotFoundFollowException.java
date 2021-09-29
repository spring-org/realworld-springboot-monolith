package com.example.realworld.application.follows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundFollowException extends RuntimeException {
    public NotFoundFollowException() {
    }

    public NotFoundFollowException(String message) {
        super(message);
    }
}
