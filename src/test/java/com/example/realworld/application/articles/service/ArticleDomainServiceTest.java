package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.tags.persistence.TagType;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

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
        User author = userRepository.save(User.of("seokrae@gmail.com", "1234", "SR"));
        User savedUser = userRepository.save(author);
        Article article = Article.of("타이틀-1", "설명", "내용", Set.of(TagType.KOTLIN), savedUser);

        // when
        articleRepository.save(article);

        // then
        assertThatExceptionOfType(NotFoundArticleException.class)
                .isThrownBy(() -> articleDomainService.getArticleOrElseThrow("no_data_slug"));
    }
}