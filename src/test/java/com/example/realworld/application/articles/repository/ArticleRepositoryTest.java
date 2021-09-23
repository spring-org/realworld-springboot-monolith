package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Article createArticle(Integer idx, User author) {
        return Article.of("title-" + idx, "description", "body", author);
    }

    private User createUser() {
        return User.of("seokrae@gmail.com", "1234", "seokrae");
    }

    private String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }

    @DisplayName("특정 사용자의 여러 글 등록 테스트")
    @Test
    void when_createArticles_expected_success_user_and_articles() {
        // given
        User author = createUser();
        User savedUser = userRepository.save(author);

        List<Article> articles = List.of(
                createArticle(1, savedUser), createArticle(2, savedUser),
                createArticle(3, savedUser), createArticle(4, savedUser));

        List<Article> savedArticles = articleRepository.saveAll(articles);

        savedUser.postArticles(savedArticles);

        int userArticlesSize = savedUser.getArticles().size();
        assertThat(userArticlesSize).isEqualTo(4);
    }

    @DisplayName("특정 사용자의 등록 글 확인 테스트")
    @Test
    void when_findArticle_expected_fail_exception() {
        // given
        User author = createUser();
        User savedUser = userRepository.save(author);

        List<Article> articles = List.of(
                createArticle(1, savedUser), createArticle(2, savedUser),
                createArticle(3, savedUser), createArticle(4, savedUser));

        List<Article> savedArticles = articleRepository.saveAll(articles);

        savedUser.postArticles(savedArticles);

        assertThatExceptionOfType(NotFoundArticleException.class)
                .isThrownBy(() -> savedUser.findArticleByTitle("title"));
    }

    @DisplayName("특정 글 정보 수정 테스트")
    @Test
    void when_updateArticle_expected_success_article_info() {
        // given
        User author = createUser();
        User savedUser = userRepository.save(author);

        List<Article> articles = List.of(
                createArticle(1, savedUser), createArticle(2, savedUser),
                createArticle(3, savedUser), createArticle(4, savedUser));

        List<Article> savedArticles = articleRepository.saveAll(articles);
        savedUser.postArticles(savedArticles);

        Article article = savedUser.findArticleByTitle("title-1");

        article.update("updateTitle-1", "description", "body");

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));

        Article updatedArticle = findUser.findArticleByTitle("updateTitle-1");

        assertThat(updatedArticle.getTitle()).isEqualTo("updateTitle-1");
    }

    /**
     * 수정 정보에 title이 없는 경우 기존 데이터가 수정되어선 안된다.
     */
    @DisplayName("특정 글에 데이터 없이 정보 수정요청 테스트")
    @Test
    void when_updateArticle_expected_success_article_no_data() {
        // given
        User author = createUser();
        User savedUser = userRepository.save(author);

        List<Article> articles = List.of(
                createArticle(1, savedUser), createArticle(2, savedUser),
                createArticle(3, savedUser), createArticle(4, savedUser));

        List<Article> savedArticles = articleRepository.saveAll(articles);
        savedUser.postArticles(savedArticles);

        Article article = savedUser.findArticleByTitle("title-1");

        article.update(null, null, null);

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));

        Article updatedArticle = findUser.findArticleByTitle("title-1");

        assertThat(updatedArticle.getTitle()).isEqualTo("title-1");
    }

    @DisplayName("글 조건 조회 테스트")
    @Test
    void when_searchPageArticle_expect_success() {
        // given
        String email = "seokrae@gmail.com";
        User author = User.of(email, "1234", "SR");
        User savedUser = userRepository.save(author);

        List<RequestSaveArticle> requestSaveArticles = List.of(
                RequestSaveArticle.of("타이틀-1", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-2", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-3", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-4", "설명", "바디", List.of("Java")),
                RequestSaveArticle.of("타이틀-5", "설명", "바디", List.of("Java"))
        );

        List<Article> dummyArticles = requestSaveArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, savedUser))
                .collect(Collectors.toList());

        List<Article> articles = articleRepository.saveAll(dummyArticles);

        // when
        RequestPageCondition condition = RequestPageCondition.of("Java", email, "SR", 20, 0);
        List<Article> searchArticles = articleRepository.searchPageArticle(condition);

        assertThat(searchArticles.size()).isEqualTo(5);
        assertAll("slug",
                () -> {
                    Article actual = searchArticles.get(0);
                    Article expected = articles.get(0);
                    assertThat(actual.getSlug()).isEqualTo(makeSlug(expected.getTitle()));
                },
                () -> {
                    Article actual = searchArticles.get(1);
                    Article expected = articles.get(1);
                    assertThat(actual.getSlug()).isEqualTo(makeSlug(expected.getTitle()));
                }
        );
    }
}