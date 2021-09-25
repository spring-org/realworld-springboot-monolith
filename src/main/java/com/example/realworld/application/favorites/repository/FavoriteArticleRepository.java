package com.example.realworld.application.favorites.repository;

import com.example.realworld.application.favorites.domain.FavoriteArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticle, Long> {
}