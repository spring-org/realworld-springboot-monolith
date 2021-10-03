package com.example.realworld.application.articles.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestArticleCondition {

    private String tag;
    private String author;
    private String favorited;

    private RequestArticleCondition(String tag, String author, String favorited) {
        this.tag = tag;
        this.author = author;
        this.favorited = favorited;
    }

    public static RequestArticleCondition of(String tag, String author, String favorited) {
        return new RequestArticleCondition(tag, author, favorited);
    }
}
