package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.dto.RequestArticleCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.realworld.application.articles.ArticleFixture.*;
import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("특정 글에 데이터 없이 정보 수정요청 테스트")
    @Test
    void when_updateArticle_expected_success_article_no_data() {
        // given
        User author = createUser("seokrae@gmail.com");
        User savedUser = userRepository.save(author);

        List<Article> articles = List.of(
                createArticle(1, savedUser, Tag.of("Java")), createArticle(2, savedUser, Tag.of("Go")),
                createArticle(3, savedUser, Tag.of("JavaScript")), createArticle(4, savedUser, Tag.of("Python")));

        List<Article> savedArticles = articleRepository.saveAll(articles);
        savedUser.addArticles(savedArticles);

        Article article = savedUser.getArticleByTitle("타이틀-1")
                .orElseThrow(NotFoundArticleException::new);

        article.update(null, null, null);

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(NotFoundUserException::new);

        Article updatedArticle = findUser.getArticleByTitle("타이틀-1")
                .orElseThrow(NotFoundArticleException::new);

        assertThat(updatedArticle.getTitle()).isEqualTo("타이틀-1");
    }

    @DisplayName("글 조건 조회 테스트")
    @Test
    void when_searchPageArticle_expect_success() {
        // given
        String email = "seokrae@gmail.com";
        User author = createUser(email);
        User savedUser = userRepository.save(author);

        List<RequestSaveArticle> requestSaveArticles = List.of(
                getRequestSaveArticle(1, "Java"),
                getRequestSaveArticle(2, "Java"),
                getRequestSaveArticle(3, "Java"),
                getRequestSaveArticle(4, "Python"),
                getRequestSaveArticle(5, "JavaScript")
        );

        List<Article> dummyArticles = requestSaveArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, savedUser))
                .collect(Collectors.toList());

        List<Article> articles = articleRepository.saveAll(dummyArticles);

        // when
        RequestArticleCondition condition = RequestArticleCondition.of("Java", email, null);
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Article> searchArticles = articleRepository.searchPageArticle(condition, pageRequest);

        // then
        assertThat(searchArticles.size()).isEqualTo(3);

        Article actual = searchArticles.get(0);
        Article expected = articles.get(0);

        assertAll("first Article Assertion",
                () -> {
                    assertThat(actual.getSlug()).isEqualTo(makeSlug(expected.getTitle()));
                    assertThat(actual.getAuthor()).isEqualTo(expected.getAuthor());
                    assertThat(actual.getTags()).contains(Tag.of("Java"));
                }
        );
    }

    @DisplayName("글 조건 조회 테스트(조건 없는 경우)")
    @Test
    void when_searchPageArticle_expect_success_no_data() {
        // given
        List<Article> articles = getDummyArticles();

        // when
        RequestArticleCondition condition = RequestArticleCondition.of(null, null, null);
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Article> searchArticles = articleRepository.searchPageArticle(condition, pageRequest);

        // then
        assertThat(searchArticles.size()).isEqualTo(5);

        Article actual = searchArticles.get(0);
        Article expected = articles.get(0);

        assertAll("Article Assertion",
                () -> {
                    assertThat(actual.getSlug()).isEqualTo(makeSlug(expected.getTitle()));
                    assertThat(actual.getAuthor()).isEqualTo(expected.getAuthor());
                    assertThat(actual.getTags()).contains(Tag.of("Java"));
                }
        );
    }

    private List<Article> getDummyArticles() {
        User srUser = userRepository.save(createUser("seokrae@gmail.com"));
        User otherUser = userRepository.save(createUser("other@gmail.com", "other"));

        List<RequestSaveArticle> srArticles = List.of(
                getRequestSaveArticle(1, "Java"),
                getRequestSaveArticle(2, "Java"),
                getRequestSaveArticle(3, "JavaScript")
        );

        List<RequestSaveArticle> otherArticles = List.of(
                getRequestSaveArticle(4, "R"),
                getRequestSaveArticle(5, "PHP")
        );

        List<Article> dummySrArticles = srArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, srUser))
                .collect(Collectors.toList());

        List<Article> dummyOtherArticles = otherArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, otherUser))
                .collect(Collectors.toList());

        List<Article> dummyArticles = Stream.concat(dummySrArticles.stream(), dummyOtherArticles.stream())
                .collect(Collectors.toList());
        return articleRepository.saveAll(dummyArticles);
    }
}