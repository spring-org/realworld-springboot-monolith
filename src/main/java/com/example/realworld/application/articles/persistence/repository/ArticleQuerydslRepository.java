package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleQuerydslRepository {

    List<Article> searchPageArticle(RequestPageCondition condition);

    List<Article> searchPageFeed(String email, Pageable pageable);
}
