package com.example.realworld.application.articles.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateArticle {

    private String title;
    private String description;
    private String body;

    private RequestUpdateArticle(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public static RequestUpdateArticle of(String title, String description, String body) {
        return new RequestUpdateArticle(title, description, body);
    }
}
