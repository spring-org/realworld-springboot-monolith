package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.users.domain.User;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveArticle {

    private String title;
    private String description;
    private String body;
    private List<String> tagList;

    private RequestSaveArticle(String title, String description, String body, List<String> tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public static RequestSaveArticle of(String title, String description, String body, List<String> tagList) {
        return new RequestSaveArticle(title, description, body, tagList);
    }

    public static Article toEntity(RequestSaveArticle saveArticle, User author) {
        return Article.of(saveArticle.getTitle(), saveArticle.getDescription(), saveArticle.getBody(), author);
    }
}
