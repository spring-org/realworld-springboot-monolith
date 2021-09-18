package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        return User.of("seokrae@gmail.com", "1234");
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
}