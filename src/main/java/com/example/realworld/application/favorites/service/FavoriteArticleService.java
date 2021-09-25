package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.dto.ResponseArticle;

public interface FavoriteArticleService {

    ResponseArticle favoriteArticle(String email, String slug);

    ResponseArticle unFavoriteArticle(String email, String slug);
}
