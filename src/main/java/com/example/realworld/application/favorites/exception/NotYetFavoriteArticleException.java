package com.example.realworld.application.favorites.exception;

public class NotYetFavoriteArticleException extends RuntimeException {
    public NotYetFavoriteArticleException() {
    }

    public NotYetFavoriteArticleException(String message) {
        super(message);
    }
}
