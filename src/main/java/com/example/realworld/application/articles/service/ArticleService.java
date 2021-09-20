package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<Article> getArticles(RequestPageCondition condition);

    List<Article> getFeedArticles(String email, Pageable pageable);

    Article getArticle(String slug);

    Article postArticle(String email, RequestSaveArticle saveArticle);

    Article updateArticle(String email, String slug, RequestUpdateArticle updateArticle);

    void deleteArticle(String email, String slug);

    Article favoriteArticle(String email, String slug);

    Article unFavoriteArticle(String email, String slug);
}
