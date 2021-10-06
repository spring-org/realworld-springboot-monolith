package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.realworld.application.articles.ArticleFixture.createArticle;
import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class ArticleDomainServiceTest {

    @Autowired
    private ArticleDomainService articleDomainService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("글 조회 실패 예외 테스트")
    @Test
    void when_getArticle_expect_fail_exception() {
        // given
        User author = userRepository.save(createUser("seokrae@gmail.com"));
        User savedUser = userRepository.save(author);
        Article article = createArticle(1, savedUser, Tag.of("Kotlin"));

        // when
        articleRepository.save(article);

        // then
        assertThatExceptionOfType(NotFoundArticleException.class)
                .isThrownBy(() -> articleDomainService.getArticleOrElseThrow("no_data_slug"));
    }
}