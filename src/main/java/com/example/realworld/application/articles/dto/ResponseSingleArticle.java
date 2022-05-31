package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.dto.ResponseMultiTag;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@JsonPropertyOrder({"slug", "title", "description", "body", "tagList", "createdAt", "updatedAt", "favorited", "favoritesCount", "author"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSingleArticle {

    private final String slug;
    private final String title;
    private final String description;
    private final String body;
    @JsonUnwrapped
    private final ResponseMultiTag tagList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;
    private final boolean favorited;
    private final int favoritesCount;
    private final ResponseProfile author;

    private ResponseSingleArticle(Article article, User author) {
        this(article, author, null);
    }

    private ResponseSingleArticle(Article article, User author, User favoriteUser) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = ResponseMultiTag.from(article.tags());
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favorited = article.getFavoriteArticles().contains(favoriteUser);
        this.favoritesCount = article.getFavoriteArticles().size();
        this.author = ResponseProfile.of(author);
    }

    public static ResponseSingleArticle from(Article article) {
        return new ResponseSingleArticle(article, article.getAuthor());
    }

    public static ResponseSingleArticle of(Article article, User favoriteUser) {
        return new ResponseSingleArticle(article, article.getAuthor(), favoriteUser);
    }
}
