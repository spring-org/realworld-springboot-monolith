package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.dto.ResponseMultiTags;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseSingleArticle {

    private String slug;
    private String title;
    private String description;
    private String body;
    private ResponseMultiTags tagList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private ResponseProfile user;

    private ResponseSingleArticle(Article article, ResponseProfile user) {
        this(article, user, null);
    }

    private ResponseSingleArticle(Article article, ResponseProfile user, User favoriteUser) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = ResponseMultiTags.from(new ArrayList<>(article.getTags()));
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favorited = article.containsFavUser(favoriteUser);
        this.favoritesCount = article.getFavUserCount();
        this.user = user;
    }

    public static ResponseSingleArticle from(Article article) {
        return new ResponseSingleArticle(article, ResponseProfile.of(article.getAuthor()));
    }

    public static ResponseSingleArticle of(Article article, User favoriteUser) {
        return new ResponseSingleArticle(article, ResponseProfile.of(article.getAuthor()), favoriteUser);
    }
}
