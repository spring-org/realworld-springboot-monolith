package com.example.realworld.application.follows.service;

import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.ResponseMultiArticle;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.follows.persistence.repository.FollowRepository;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
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
class FeedBusinessServiceTest {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private FollowBusinessService followBusinessService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        followRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @DisplayName("팔로우한 사용자의 글을 피드 테스트")
    @Test
    void when_searchPageFeed_expect_success_articles() {
        // given
        String email = "seokrae@gmail.com";
        String otherUserEmail = "other@gmail.com";
        dummyArticle(email, otherUserEmail);

        // when
        followBusinessService.follow(email, otherUserEmail);

        Pageable page = Pageable.ofSize(10);
        ResponseMultiArticle feedArticles = articleService.getFeedArticles(email, page);

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
        followBusinessService.follow(email, otherUserEmail);
        Pageable page = Pageable.ofSize(10);

        // then
        assertThatExceptionOfType(NotFoundUserException.class)
                .isThrownBy(() -> articleService.getFeedArticles(theOtherUserEmail, page));
    }

    private void dummyArticle(String email, String otherUserEmail) {
        User author = userDomainService.save(UserFactory.of(email, "1234", "SR"));
        User otherUser = userDomainService.save(UserFactory.of(otherUserEmail, "1234", "seok"));

        List<RequestSaveArticle> srArticles = List.of(
                RequestSaveArticle.of("타이틀-1", "설명", "내용", "Java"),
                RequestSaveArticle.of("타이틀-2", "설명", "내용", "JavaScript"),
                RequestSaveArticle.of("타이틀-3", "설명", "내용", "Python")
        );

        List<RequestSaveArticle> seokArticles = List.of(
                RequestSaveArticle.of("타이틀-4", "설명", "내용", "Java"),
                RequestSaveArticle.of("타이틀-5", "설명", "내용", "JavaScript")
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
