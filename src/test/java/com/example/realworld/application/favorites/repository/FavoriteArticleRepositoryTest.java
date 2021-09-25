package com.example.realworld.application.favorites.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.favorites.domain.FavoriteArticle;
import com.example.realworld.application.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteArticleRepositoryTest {

    @Autowired
    private FavoriteArticleRepository favoriteArticleRepository;

    @DisplayName("특정 글의 좋아요 처리 테스트")
    @Test
    void when_saveFavoriteArticle_expect_success_save() {
        User author = User.of("seokrae@gmail.com", "1234", "seokrae");
        Article article = Article.of("title-1", "description", "body", author);
        FavoriteArticle favoriteArticle = FavoriteArticle.of(author, article);

        FavoriteArticle savedFavoriteArticle = favoriteArticleRepository.save(favoriteArticle);

        assertThat(savedFavoriteArticle.getFavoriteUser()).isEqualTo(author);
        assertThat(savedFavoriteArticle.getFavoritedArticle()).isEqualTo(article);
    }
}