package com.example.realworld.application.follows.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.ResponseMultiArticles;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.follows.exception.DuplicateFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.repository.FollowRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class FollowBusinessServiceTest {

    @Autowired
    private FollowBusinessService followBusinessService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        followRepository.deleteAll();
    }

    @DisplayName("사용자 간의 팔로우 테스트")
    @Test
    void when_follow_expect_success_user_relationship() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        ResponseProfile responseProfile = followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());

        // then
        assertThat(responseProfile.isFollowing()).isTrue();
    }

    @DisplayName("사용자 간의 팔로우 예외 테스트")
    @Test
    void when_follow_expect_fail_duplicate_follow() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        String fromEmail = fromUser.getEmail();
        String toEmail = toUser.getEmail();
        followBusinessService.followUser(fromEmail, toEmail);

        // then
        assertThatExceptionOfType(DuplicateFollowException.class)
                .isThrownBy(() -> followBusinessService.followUser(fromEmail, toEmail));
    }

    @DisplayName("사용자 간의 언팔로우 테스트")
    @Test
    void when_unFollow_expect_success_single() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        User savedFromUser = userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());
        followBusinessService.unFollow(fromUser.getEmail(), toUser.getEmail());

        // then
        assertThat(savedFromUser.isFollowing(toUser)).isFalse();
    }

    @DisplayName("사용자 간의 언팔로우 예외 테스트")
    @Test
    void when_unFollow_expect_fail_not_found_follow_exception() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        User savedFromUser = userRepository.save(fromUser);
        User savedToUser = userRepository.save(toUser);

        // when
        String fromEmail = savedFromUser.getEmail();
        String toEmail = savedToUser.getEmail();

        // then
        assertThatExceptionOfType(NotFoundFollowException.class)
                .isThrownBy(() -> followBusinessService.unFollow(fromEmail, toEmail));
    }

    @DisplayName("팔로우한 사용자의 글을 피드 테스트")
    @Test
    void when_searchPageFeed_expect_success_articles() {
        // given
        String email = "seokrae@gmail.com";
        String otherUserEmail = "other@gmail.com";
        dummyArticle(email, otherUserEmail);

        // when
        followBusinessService.followUser(email, otherUserEmail);

        Pageable page = Pageable.ofSize(10);
        ResponseMultiArticles feedArticles = articleService.getFeedArticles(email, page);

        // then
        assertThat(feedArticles.getArticleCount()).isNotZero();
    }

    @DisplayName("팔로우한 사용자의 글을 피드 예외 테스트")
    @Test
    void when_searchPageFeed_expect_fail_not_found_user_exception() {
        // given
        String email = "seokrae@gmail.com";
        String otherUserEmail = "other@gmail.com";
        String theOtherUserEmail = "notFoundUser@gmail.com";
        dummyArticle(email, otherUserEmail);

        // when
        followBusinessService.followUser(email, otherUserEmail);
        Pageable page = Pageable.ofSize(10);

        // then
        assertThatExceptionOfType(NotFoundUserException.class)
                .isThrownBy(() -> articleService.getFeedArticles(theOtherUserEmail, page));
    }

    private void dummyArticle(String email, String otherUserEmail) {
        User author = userRepository.save(User.of(email, "1234", "SR"));
        User otherUser = userRepository.save(User.of(otherUserEmail, "1234", "seok"));

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
                .map(request -> RequestSaveArticle.toEntity(request, author))
                .collect(Collectors.toList());

        List<Article> dummySeokArticles = seokArticles.stream()
                .map(request -> RequestSaveArticle.toEntity(request, otherUser))
                .collect(Collectors.toList());

        List<Article> dummyArticles = Stream.concat(dummySrArticles.stream(), dummySeokArticles.stream())
                .collect(Collectors.toList());
        articleRepository.saveAll(dummyArticles);
    }

}