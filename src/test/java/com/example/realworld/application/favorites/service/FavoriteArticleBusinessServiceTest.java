package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.ResponseSingleArticle;
import com.example.realworld.application.articles.exception.DuplicateFavoriteArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.favorites.exception.NotYetFavoriteArticleException;
import com.example.realworld.application.favorites.persistence.repository.FavoriteArticleRepository;
import com.example.realworld.application.tags.persistence.TagType;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class FavoriteArticleBusinessServiceTest {

    @Autowired
    private FavoriteArticleService favoriteArticleService;
    @Autowired
    private FavoriteArticleRepository favoriteArticleRepository;
    @Autowired
    private ArticleService articleService;
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
    void when_favArticle_expect_success_favorite() {
        // given
        String email = "me@gmail.com";
        User me = User.of(email, "1234", "myName");
        User otherUser = User.of("other@gmail.com", "1234", "otherName");
        userRepository.save(me);
        User savedOtherUser = userRepository.save(otherUser);

        // when
        // 다른 사람이 작성한 글 등록 및 저장
        Article otherUserWriteArticle = Article.of("title-1", "description", "body", Set.of(TagType.KOTLIN), savedOtherUser);
        Article savedOtherUserWriteArticle = articleRepository.save(otherUserWriteArticle);
        savedOtherUser.addArticle(savedOtherUserWriteArticle);

        // 다른 사람의 글을 좋아요 처리
        ResponseSingleArticle responseSingleArticle = favoriteArticleService.favoriteArticle(email, savedOtherUserWriteArticle.getSlug());

        // then
        assertThat(responseSingleArticle.isFavorited()).isTrue();
    }

    @DisplayName("사용자의 글 좋아요 중복 예외 테스트")
    @Test
    void when_duplicateFavoriteArticle_expect_fail_exception() {
        // given
        String email = "me@gmail.com";
        User me = User.of(email, "1234", "myName");
        User otherUser = User.of("other@gmail.com", "1234", "otherName");

        userRepository.save(me);
        userRepository.save(otherUser);

        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

        // when
        // 다른 사람이 작성한 글 등록 및 저장
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(otherUser.getEmail(), requestSaveArticle);

        String slug = responseSingleArticle.getSlug();
        favoriteArticleService.favoriteArticle(email, slug);

        assertThatExceptionOfType(DuplicateFavoriteArticleException.class)
                .isThrownBy(() -> favoriteArticleService.favoriteArticle(email, slug));
    }

    @DisplayName("사용자의 글 좋아요 취소 테스트")
    @Test
    void when_unFavArticle_expect_success_un_favorite_article() {
        // given
        String email = "me@gmail.com";
        User me = User.of(email, "1234", "myName");
        User otherUser = User.of("other@gmail.com", "1234", "otherName");

        // 사용자 둘 등록
        userRepository.save(me);
        userRepository.save(otherUser);

        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

        // when
        // 다른 사람이 작성한 글 등록 및 저장
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(otherUser.getEmail(), requestSaveArticle);

        String slug = responseSingleArticle.getSlug();
        favoriteArticleService.favoriteArticle(email, slug);

        favoriteArticleService.unFavoriteArticle(email, slug);
        long existsFavArticle = favoriteArticleRepository.count();

        // then
        assertThat(existsFavArticle).isZero();
    }

    @DisplayName("사용자의 글 좋아요 취소 예외 테스트")
    @Test
    void when_unFavArticle_expect_fail_not_yet_favorite() {
        // given
        String email = "me@gmail.com";
        User me = User.of(email, "1234", "myName");
        User otherUser = User.of("other@gmail.com", "1234", "otherName");
        userRepository.save(me);
        User savedOtherUser = userRepository.save(otherUser);

        // when
        // 다른 사람이 작성한 글 등록 및 저장
        Article otherUserWriteArticle1 = Article.of("title-1", "description", "body", Set.of(TagType.JAVASCRIPT), savedOtherUser);
        Article savedOtherUserWriteArticle1 = articleRepository.save(otherUserWriteArticle1);
        savedOtherUser.addArticle(savedOtherUserWriteArticle1);

        String slug = savedOtherUserWriteArticle1.getSlug();

        assertThatExceptionOfType(NotYetFavoriteArticleException.class)
                .isThrownBy(() -> favoriteArticleService.unFavoriteArticle(email, slug));
    }
}