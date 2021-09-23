package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ArticleBusinessService.class)
class ArticleBusinessServiceTest {

    @Autowired
    private ArticleBusinessService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private UserRepository userRepository;

    private String email;
    private RequestSaveArticle saveArticle;
    private User author;
    private Article article;


    private String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }

    @BeforeEach
    void setUp() {
        email = "seokrae@gmail.com";
        saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));
        author = User.of(email, "1234");
        article = RequestSaveArticle.toEntity(saveArticle, author);
    }

    @DisplayName("글 등록 테스트")
    @Test
    void when_createArticle_expect_success_confirm_slug() {
        // given

        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(author));
        when(articleRepository.save(any())).thenReturn(article);

        ResponseArticle responseArticle = articleService.postArticle(email, saveArticle);

        // then
        String expectedSlug = makeSlug(saveArticle.getTitle());
        assertThat(responseArticle.getSlug()).isEqualTo(expectedSlug);
    }
}