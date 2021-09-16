package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}