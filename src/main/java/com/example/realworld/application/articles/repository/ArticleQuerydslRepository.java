package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleQuerydslRepository {

    List<Article> searchPageArticle(RequestPageCondition condition);

    List<Article> searchPageFeed(String email, Pageable pageable);
}
