package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.dto.ResponseSingleArticle;

public interface FavoriteArticleService {
    // 특정 글 관심 리스트에 등록
    ResponseSingleArticle favoriteArticle(String email, String slug);

    // 관심 리스트에 있는 특정 글 삭제
    ResponseSingleArticle unFavoriteArticle(String email, String slug);
}
