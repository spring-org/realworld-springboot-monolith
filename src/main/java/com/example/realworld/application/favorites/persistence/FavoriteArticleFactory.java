package com.example.realworld.application.favorites.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.users.persistence.User;

public class FavoriteArticleFactory {

    private FavoriteArticleFactory() {}

    public static FavoriteArticle of(User fromUser, Article article) {
        return new FavoriteArticle(fromUser, article);
    }
}