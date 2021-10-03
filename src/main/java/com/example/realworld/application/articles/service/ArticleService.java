package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.*;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ResponseMultiArticle searchPageArticles(RequestArticleCondition condition, Pageable pageable);

    ResponseMultiArticle getFeedArticles(String email, Pageable pageable);

    ResponseSingleArticle getArticle(String slug);

    ResponseSingleArticle postArticle(String email, RequestSaveArticle saveArticle);

    ResponseSingleArticle updateArticle(String email, String slug, RequestUpdateArticle updateArticle);

    void deleteArticle(String email, String slug);
}
