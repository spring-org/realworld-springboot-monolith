package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    private static User createUser() {
        return User.of("seokrae@gmail.com", "1234");
    }

    @DisplayName("사용자 생성 및 포스팅 작성 테스트")
    @Test
    void when_createArticle_expected_success_user_and_article() {
        // given
        User author = createUser();

        // when
        Article article = Article.of(
                "title", "description", "body", author, Tag.of("Kotlin"));

        // then
        assertThat(article.author()).isEqualTo(author.userName());
        assertThat(article.getTitle()).isEqualTo("title");
    }
}