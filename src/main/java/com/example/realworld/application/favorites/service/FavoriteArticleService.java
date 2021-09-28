package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.dto.ResponseArticle;

public interface FavoriteArticleService {
    // 특정 글 관심 리스트에 등록
    ResponseArticle favoriteArticle(String email, String slug);

    // 관심 리스트에 있는 특정 글 삭제
    ResponseArticle unFavoriteArticle(String email, String slug);
}
