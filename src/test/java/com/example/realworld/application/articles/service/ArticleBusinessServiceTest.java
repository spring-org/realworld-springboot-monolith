package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ArticleBusinessService.class)
class ArticleBusinessServiceTest {

    @Autowired
    private ArticleBusinessService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private UserRepository userRepository;

    private String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }

    @DisplayName("글 등록 테스트")
    @Test
    void when_createArticle_expect_success_confirm_slug() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));
        User author = User.of(email, "1234");
        Article article = RequestSaveArticle.toEntity(saveArticle, author);

        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(author));
        when(articleRepository.save(any())).thenReturn(article);

        ResponseArticle responseArticle = articleService.postArticle(email, saveArticle);

        // then
        String expectedSlug = makeSlug(saveArticle.getTitle());
        assertThat(responseArticle.getSlug()).isEqualTo(expectedSlug);
    }

    @DisplayName("글 조회 테스트")
    @Test
    void when_findArticle_expect_success_equals_slug() {
        // given
        String email = "seokrae@gmail.com";
        User author = User.of(email, "1234");

        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));
        Article article = RequestSaveArticle.toEntity(saveArticle, author);

        String slug = makeSlug(saveArticle.getTitle());

        // when
        when(articleRepository.findBySlug(any())).thenReturn(Optional.of(article));

        ResponseArticle actual = articleService.getArticle(slug);

        // then
        assertThat(actual.getSlug()).isEqualTo(makeSlug(actual.getTitle()));
    }
}