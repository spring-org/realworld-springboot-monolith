package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.persistence.TagType;
import com.example.realworld.application.users.persistence.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveArticle {

    private String title;
    private String description;
    private String body;
    private Set<TagType> tagList;

    private RequestSaveArticle(String title, String description, String body, Set<TagType> tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public static RequestSaveArticle of(String title, String description, String body, Set<TagType> tagList) {
        return new RequestSaveArticle(title, description, body, tagList);
    }

    public static Article toEntity(RequestSaveArticle saveArticle, User author) {
        return Article.of(saveArticle.getTitle(), saveArticle.getDescription(), saveArticle.getBody(), saveArticle.getTagList(), author);
    }
}
