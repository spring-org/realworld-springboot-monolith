package com.example.realworld.application.articles.exception;

public class DuplicateFavoriteArticleException extends RuntimeException {
    public DuplicateFavoriteArticleException() {
    }

    public DuplicateFavoriteArticleException(String message) {
        super(message);
    }
}
