package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("글에 새로운 커멘트 등록하는 테스트")
    @Test
    void when_postComment_expected_success_posting_comment() {
        // given
        User author = User.of("seokrae@gmail.com", "1234");
        User savedUser = userRepository.save(author);
        Article article = Article.of("title", "description", "body", savedUser);
        Article savedArticle = articleRepository.save(article);

        // when
        Comment newComment = Comment.of("comment write", savedUser);
        savedArticle.postComment(newComment);
        Comment savedComment = commentRepository.save(newComment);

        // then
        assertThat(newComment.getBody()).isEqualTo(savedComment.getBody());
    }

    @DisplayName("커멘트 수정 확인 테스트")
    @Test
    void when_update_expected_success_comment_info() {
        // given
        User author = User.of("seokrae@gmail.com", "1234");
        User savedUser = userRepository.save(author);

        Article article = Article.of("title", "description", "body", savedUser);
        Article savedArticle = articleRepository.save(article);


        Comment newComment = Comment.of("comment write", savedUser);
        Comment savedComment = commentRepository.save(newComment);
        savedArticle.postComment(savedComment);
        savedUser.postArticles(savedArticle);

        // when
        savedComment.update("updateBody");

        // then
        String actual = savedUser.findArticleByTitle("title")
                .findComment(savedComment.getBody()).getBody();

        assertThat(actual).isEqualTo(savedComment.getBody());

    }
}