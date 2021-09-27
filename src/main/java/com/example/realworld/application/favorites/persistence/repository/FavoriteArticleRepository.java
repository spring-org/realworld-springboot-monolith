package com.example.realworld.application.favorites.persistence.repository;

import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticle, Long> {
}