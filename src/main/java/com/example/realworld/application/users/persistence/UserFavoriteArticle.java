package com.example.realworld.application.users.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.favorites.exception.NotFoundFavoriteArticleException;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;

import java.io.Serializable;

public class UserFavoriteArticle implements Serializable {
    private final User user;

    public UserFavoriteArticle(User user) {
        this.user = user;
    }

    // 글 좋아요 처리
    public Article favArticle(FavoriteArticle newFavArticle) {
        user.getFavoriteArticles().add(newFavArticle);
        Article article = newFavArticle.article();
        return article.addFavArticle(newFavArticle);
    }// 글 좋아요 취소 처리

    public Article unFavArticle(FavoriteArticle favoriteArticle) {
        user.getFavoriteArticles().remove(favoriteArticle);
        Article article = favoriteArticle.article();
        return article.removeFavArticle(favoriteArticle);
    }// 관심 글 확인

    public boolean isMatchesArticleBySlug(String slug) {
        return user.getFavoriteArticles().stream()
                .anyMatch(favArticle -> favArticle.isMatchesArticleBySlug(slug));
    }// 관심 글 Slug로 검색

    public FavoriteArticle getFavArticle(String slug) {
        return user.getFavoriteArticles().stream()
                .filter(favArticle -> favArticle.isMatchesArticleBySlug(slug))
                .findFirst()
                .orElseThrow(NotFoundFavoriteArticleException::new);
    }
}