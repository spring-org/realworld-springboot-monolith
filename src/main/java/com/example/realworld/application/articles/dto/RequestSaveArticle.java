package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.persistence.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveArticle {

    @NotEmpty(message = "title is not empty")
    private String title;
    @NotEmpty(message = "description is not empty")
    private String description;
    @NotEmpty(message = "body is not empty")
    private String body;
    private String[] tagList;

    private RequestSaveArticle(String title, String description, String body, String... tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public static RequestSaveArticle of(String title, String description, String body, String... tagList) {
        return new RequestSaveArticle(title, description, body, tagList);
    }

    public static Article toEntity(RequestSaveArticle saveArticle, User author) {
        Tag[] tags = Arrays.stream(saveArticle.getTagList())
                .map(Tag::of)
                .toArray(Tag[]::new);
        return Article.of(saveArticle.getTitle(), saveArticle.getDescription(), saveArticle.getBody(), author, tags);
    }
}
