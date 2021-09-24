package com.example.realworld.application.articles.dto;

import lombok.Getter;

@Getter
public class RequestUpdateArticle {

    private final String title;
    private final String description;
    private final String body;

    private RequestUpdateArticle(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public static RequestUpdateArticle of(String title, String description, String body) {
        return new RequestUpdateArticle(title, description, body);
    }
}
