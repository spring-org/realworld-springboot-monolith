package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class ArticleBusinessServiceTest {

    @Autowired
    private ArticleBusinessService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @DisplayName("글 등록 테스트")
    @Test
    void when_createArticle_expect_success_confirm_slug() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));
        User author = User.of(email, "1234", "seokrae");
        Article article = RequestSaveArticle.toEntity(saveArticle, author);

        // when
        userRepository.save(author);
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
        User user = User.of(email, "1234", "seokrae");
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));

        // when
        userRepository.save(user);
        ResponseArticle postArticle = articleService.postArticle(email, saveArticle);
        ResponseArticle actual = articleService.getArticle(postArticle.getSlug());

        // then
        assertThat(actual.getSlug()).isEqualTo(makeSlug(saveArticle.getTitle()));
    }

    @DisplayName("글 수정 테스트")
    @Test
    void when_updateArticle_expect_success_update_article_info() {
        // given
        String email = "seokrae@gmail.com";
        User user = User.of(email, "1234", "seokrae");
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));
        RequestUpdateArticle updateArticle = RequestUpdateArticle.of("수정된_타이틀", "설명", "바디");

        // when
        userRepository.save(user);
        ResponseArticle postArticle = articleService.postArticle(email, saveArticle);
        ResponseArticle actual = articleService.updateArticle(user.getEmail(), makeSlug(postArticle.getTitle()), updateArticle);

        // then
        String expected = makeSlug(updateArticle.getTitle());
        assertThat(actual.getSlug()).isEqualTo(expected);
    }

    @DisplayName("글 삭제 테스트")
    @Test
    void when_deleteArticle_expect_success_deleted_article() {
        // given
        String email = "seokrae@gmail.com";
        User user = User.of(email, "1234", "seokrae");
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));

        // when
        userRepository.save(user);
        ResponseArticle postArticle = articleService.postArticle(email, saveArticle);
        articleService.deleteArticle(email, postArticle.getSlug());

        Optional<Article> existsArticle = articleRepository.findBySlug(postArticle.getSlug());

        // then
        assertThat(existsArticle).isEmpty();
    }

    @DisplayName("글 삭제 실패 테스트")
    @Test
    void when_deleteArticle_expect_fail_exception() {
        // given
        String email = "seokrae@gmail.com";
        User user = User.of(email, "1234", "seokrae");
        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "바디", List.of("java"));

        // when
        userRepository.save(user);
        articleService.postArticle(email, saveArticle);

        // then
        assertThatExceptionOfType(NotFoundArticleException.class)
                .isThrownBy(() -> articleService.deleteArticle(email, "illegal_slug"));
    }

    @DisplayName("글 조건 조회 및 페이징 테스트")
    @Test
    void when_searchPage_expect_success_condition() {
        // given
        List<Article> dummyArticles = getDummyArticles();
        RequestPageCondition requestCondition = RequestPageCondition.of("", "", "seok", 20, 0);

        // when
        ResponseMultiArticles responseMultiArticles = articleService.searchPageArticles(requestCondition);
        int articleCount = responseMultiArticles.getArticleCount();

        // then
        assertThat(articleCount).isEqualTo(2);
    }

    private List<Article> getDummyArticles() {
        User sr = userRepository.save(User.of("seokrae@gmail.com", "1234", "SR"));
        User seok = userRepository.save(User.of("other@gmail.com", "1234", "seok"));

        List<RequestSaveArticle> srArticles = List.of(
                RequestSaveArticle.of("타이틀-1", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-2", "설명", "바디", List.of("javascript")),
                RequestSaveArticle.of("타이틀-3", "설명", "바디", List.of("node.js"))
        );

        List<RequestSaveArticle> seokArticles = List.of(
                RequestSaveArticle.of("타이틀-4", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-5", "설명", "바디", List.of("javascript"))
        );

        List<Article> dummySrArticles = srArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, sr))
                .collect(Collectors.toList());

        List<Article> dummySeokArticles = seokArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, seok))
                .collect(Collectors.toList());

        List<Article> dummyArticles = Stream.concat(dummySrArticles.stream(), dummySeokArticles.stream())
                .collect(Collectors.toList());
        return articleRepository.saveAll(dummyArticles);
    }

    @DisplayName("사용자의 글 좋아요 처리 테스트")
    @Test
    void when_favoriteArticle_expect_success_add_article() {
        getDummyArticles();

        ResponseArticle responseArticle = articleService.favoriteArticle("seokrae@gmail.com", makeSlug("타이틀-1"));

        assertThat(responseArticle.getFavoritesCount()).isOne();
    }
}