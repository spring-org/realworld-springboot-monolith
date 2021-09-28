package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.*;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ResponseMultiArticles searchPageArticles(RequestPageCondition condition);

    ResponseMultiArticles getFeedArticles(String email, Pageable pageable);

    ResponseArticle getArticle(String slug);

    ResponseArticle postArticle(String email, RequestSaveArticle saveArticle);

    ResponseArticle updateArticle(String email, String slug, RequestUpdateArticle updateArticle);

    void deleteArticle(String email, String slug);
}
