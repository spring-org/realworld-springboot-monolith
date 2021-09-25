package com.example.realworld.application.articles.domain;

import com.example.realworld.application.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @DisplayName("커멘트 등록 확인 테스트")
    @Test
    void when_postComment_expected_success_entity() {
        // given
        User author = User.of("seokrae@gmail.com", "1234");
        Article article = Article.of("title", "description", "body", author);
        Comment actualComment = Comment.of("comment write", author, article);

        // when
        article.addComment(actualComment);
        int actualCommentSize = article.getComments().size();

        // then
        assertThat(actualCommentSize).isOne();
    }
}