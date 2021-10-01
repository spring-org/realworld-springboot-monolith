package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundCommentException;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.articles.persistence.repository.CommentRepository;
import com.example.realworld.application.tags.persistence.TagType;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.core.exception.UnauthorizedUserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class CommentBusinessServiceTest {
    @Autowired
    private CommentBusinessService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("특정 글에 커멘트 등록하는 테스트")
    @Test
    void when_postComment_expect_success_comments() {
        // given
        String authEmail = "seok@gmail.com";
        User authorUser = User.of(authEmail, "1234", "seok");
        User savedAuthorUser = userRepository.save(authorUser);

        User otherUser = User.of("other@gmail.com", "1234", "otherName");
        User savedOtherUser = userRepository.save(otherUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(authEmail, RequestSaveArticle.of("타이틀-1", "설명", "글 내용", Set.of(TagType.KOTLIN)));

        // when
        ResponseSingleComment responseComment = commentService.postComment(
                savedOtherUser.getEmail(), responseSingleArticle.getSlug(), RequestSaveComment.from("내용"));

        // then
        assertThat(responseComment.getAuthor().getUserName()).isEqualTo(savedOtherUser.getProfile().getUserName());
    }

    @DisplayName("특정 글에 등록된 커멘트를 조회하는 테스트")
    @Test
    void when_getCommentsByArticle_expect_success_comments() {
        // given
        String authEmail = "seok@gmail.com";
        User authorUser = User.of(authEmail, "1234", "seok");
        userRepository.save(authorUser);

        String otherUserEmail = "other@gmail.com";
        User otherUser = User.of(otherUserEmail, "1234", "otherName");
        userRepository.save(otherUser);
        ResponseSingleArticle responseArticle = articleService.postArticle(
                authEmail, RequestSaveArticle.of("타이틀-1", "설명", "글 내용", Set.of(TagType.PHP)));

        // when
        commentService.postComment(otherUserEmail, responseArticle.getSlug(), RequestSaveComment.from("커멘트 내용1"));
        commentService.postComment(otherUserEmail, responseArticle.getSlug(), RequestSaveComment.from("커멘트 내용2"));

        ResponseMultiComment commentsByArticle = commentService.getCommentsByArticle(responseArticle.getSlug());

        // then
        assertThat(commentsByArticle.getCommentSize()).isEqualTo(2);
    }

    @DisplayName("특정 글의 커멘트 삭제 테스트")
    @Test
    void when_deleteComment_expect_success_delete_comment() {
        // given
        String authEmail = "seok@gmail.com";
        User authorUser = User.of(authEmail, "1234", "seok");
        userRepository.save(authorUser);

        String otherUserEmail = "other@gmail.com";
        User otherUser = User.of(otherUserEmail, "1234", "otherName");
        userRepository.save(otherUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(authEmail, RequestSaveArticle.of("타이틀-1", "설명", "글 내용", Set.of(TagType.RUBY)));

        // when
        ResponseSingleComment singleComment = commentService.postComment(otherUserEmail, responseSingleArticle.getSlug(), RequestSaveComment.from("커멘트 내용1"));
        commentService.postComment(otherUserEmail, responseSingleArticle.getSlug(), RequestSaveComment.from("커멘트 내용2"));

        commentService.deleteComment(otherUserEmail, responseSingleArticle.getSlug(), singleComment.getId());

        ResponseMultiComment commentsByArticle = commentService.getCommentsByArticle(responseSingleArticle.getSlug());

        // then
        assertThat(commentsByArticle.getCommentSize()).isOne();
    }

    @DisplayName("특정 글의 커멘트 삭제 (권한 예외) 테스트")
    @Test
    void when_deleteComment_expect_fail_unAuthorization_exception() {
        // given
        String authEmail = "seok@gmail.com";
        User authorUser = User.of(authEmail, "1234", "seok");
        userRepository.save(authorUser);

        String otherUserEmail = "other@gmail.com";
        User otherUser = User.of(otherUserEmail, "1234", "otherName");
        userRepository.save(otherUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(authEmail, RequestSaveArticle.of("타이틀-1", "설명", "글 내용", Set.of(TagType.R)));

        // when
        String slug = responseSingleArticle.getSlug();
        Long id = commentService.postComment(otherUserEmail, slug, RequestSaveComment.from("커멘트 내용1")).getId();
        commentService.postComment(otherUserEmail, slug, RequestSaveComment.from("커멘트 내용2"));

        // then
        assertThatExceptionOfType(UnauthorizedUserException.class)
                .isThrownBy(() -> commentService.deleteComment(authEmail, slug, id));
    }

    @DisplayName("특정 글의 커멘트 삭제 (존재하지 않는 커멘트) 테스트")
    @Test
    void when_deleteComment_expect_fail_not_exists_comment_exception() {
        // given
        String authEmail = "seok@gmail.com";
        User authorUser = User.of(authEmail, "1234", "seok");
        userRepository.save(authorUser);

        String otherUserEmail = "other@gmail.com";
        User otherUser = User.of(otherUserEmail, "1234", "otherName");
        userRepository.save(otherUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(authEmail, RequestSaveArticle.of("타이틀-1", "설명", "글 내용", Set.of(TagType.CPP)));

        // when
        String slug = responseSingleArticle.getSlug();
        commentService.postComment(otherUserEmail, slug, RequestSaveComment.from("커멘트 내용1")).getId();
        commentService.postComment(otherUserEmail, slug, RequestSaveComment.from("커멘트 내용2"));
        long notExistsCommentId = 2L;
        // then
        assertThatExceptionOfType(NotFoundCommentException.class)
                .isThrownBy(() -> commentService.deleteComment(authEmail, slug, notExistsCommentId));
    }
}