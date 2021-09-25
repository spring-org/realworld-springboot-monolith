package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.favorites.repository.FavoriteArticleRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FavoriteArticleServiceTest {

    @Autowired
    private FavoriteArticleService favoriteArticleService;
    @Autowired
    private FavoriteArticleRepository favoriteArticleRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        favoriteArticleRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("사용자의 글 좋아요 기능 테스트")
    @Test
    void testCase1() {
        // 사용자 저장
        String email = "me@gmail.com";
        User me = User.of(email, "1234", "myName");
        User otherUser = User.of("other@gmail.com", "1234", "otherName");
        User savedMe = userRepository.save(me);
        User savedOtherUser = userRepository.save(otherUser);

        // 다른 사람이 작성한 글 등록 및 저장
        Article otherUserWriteArticle = Article.of("title-1", "description", "body", savedOtherUser);
        Article savedOtherUserWriteArticle = articleRepository.save(otherUserWriteArticle);
        savedOtherUser.postArticles(savedOtherUserWriteArticle);

        // 다른 사람의 글을 좋아요 처리
        ResponseArticle responseArticle = favoriteArticleService.favoriteArticle(email, savedOtherUserWriteArticle.getSlug());

        //
        assertThat(responseArticle.isFavorited()).isTrue();
    }
}