package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.tags.domain.Tag;
import com.example.realworld.application.users.dto.ResponseProfile;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class ResponseArticle {

    private final String slug;
    private final String title;
    private final String description;
    private final String body;
    private final Set<Tag> tagList;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean favorited;
    private final int favoritesCount;
    private final ResponseProfile user;

    private ResponseArticle(Article article, ResponseProfile user) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = article.getTags();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favorited = article.isFavorites();
        this.favoritesCount = article.getFavoritesCount();
        this.user = user;
    }

    public static ResponseArticle of(Article article) {
        return new ResponseArticle(article, ResponseProfile.of(article.getAuthor()));
    }
}
