package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.persistence.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleQuerydslRepository {

    Optional<Article> findBySlugOrderByIdDesc(String slug);

}