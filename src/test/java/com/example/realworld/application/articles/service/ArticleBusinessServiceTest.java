package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.realworld.application.articles.ArticleFixture.*;
import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleBusinessServiceTest {

    @InjectMocks
    private ArticleBusinessService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleDomainService articleDomainService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDomainService userDomainService;

    @DisplayName("글 등록 테스트")
    @Test
    void when_createArticle_expect_success_confirm_slug() {
        // given
        String email = "seokrae@gmail.com";
        User author = createUser(email);
	    Article article = Article.of("title", "desc", "body", author, Tag.of("태그"));

		given(userDomainService.findUserByEmail(anyString())).willReturn(author);
	    given(articleDomainService.addArticle(any(), any())).willReturn(article);

        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, any());

		then(userDomainService).should().findUserByEmail(anyString());
		then(articleDomainService).should().addArticle(any(), any());

		assertThat(responseSingleArticle.getSlug()).isEqualTo(makeSlug(article.getTitle()));
    }

    @DisplayName("글 조회 테스트")
    @Test
    void when_findArticle_expect_success_equals_slug() {
        // given
        String email = "seokrae@gmail.com";
        User author = createUser(email);
	    Article article = Article.of("title", "desc", "body", author, Tag.of("태그"));

		given(articleDomainService.getArticleBySlug(anyString())).willReturn(article);

		ResponseSingleArticle actual = articleService.getArticle(article.getSlug());

        then(articleDomainService).should().getArticleBySlug(anyString());
        assertThat(actual.getSlug()).isEqualTo(makeSlug(article.getTitle()));
    }

//    @DisplayName("글 수정 테스트")
//    @Test
//    void when_updateArticle_expect_success_update_article_info(
//		    @Mock User author, @Mock Article article,
//		    @Mock RequestUpdateArticle updatedArticle,
//		    @Mock ResponseSingleArticle singleArticle,
//		    @Mock ResponseProfile responseProfile) {
//        // given
//        String email = "seokrae@gmail.com";
////        User author = createUser(email);
////	    Article article = Article.of("title", "desc", "body", author, Tag.of("태그"));
//
//		given(userDomainService.findUserByEmail(any())).willReturn(author);
//		given(author.getArticleBySlug(article.getSlug())).willReturn(Optional.of(article));
//		given(Article.of(anyString(), anyString(), anyString(), any(), any())).willReturn(article);
//		given(ResponseSingleArticle.from(any())).willReturn(singleArticle);
//		given(ResponseProfile.of(any())).willReturn(responseProfile);
//
//        ResponseSingleArticle actual = articleService.updateArticle(
//				author.getEmail(),
//		        article.getSlug(),
////		        getRequestUpdateArticle("수정된_타이틀", "설명", "내용")
//		        updatedArticle
//        );
//
//        // then
////        String expected = makeSlug(updateArticle.getTitle());
////        assertThat(actual.getSlug()).isEqualTo(expected);
//    }
//
//    @DisplayName("글 수정 예외(존재하지 않는 글) 테스트")
//    @Test
//    void when_updateArticle_expect_fail_not_found_article() {
//        // given
//        String email = "seokrae@gmail.com";
//        User user = createUser(email);
//        RequestSaveArticle saveArticle = getRequestSaveArticle(1, "Java");
//        RequestUpdateArticle updateArticle = getRequestUpdateArticle("수정된_타이틀", "설명", "내용");
//
//        // when
//        userRepository.save(user);
//        articleService.postArticle(email, saveArticle);
//        String not_found_article_title = makeSlug("not_found_article_title");
//        // then
//        assertThatExceptionOfType(NotFoundArticleException.class)
//                .isThrownBy(() -> articleService.updateArticle(email, not_found_article_title, updateArticle));
//    }
//
//    // 갑자기 delete 처리가 안되는 문제 발생
//    @DisplayName("글 삭제 테스트")
//    @Test
//    void when_deleteArticle_expect_success_deleted_article() {
//        // given
//        String email = "seokrae@gmail.com";
//        User user = createUser(email);
//        RequestSaveArticle saveArticle = getRequestDeletedArticle("삭제 타이틀", "Java");
//
//        // when
//        userRepository.save(user);
//        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, saveArticle);
//        articleService.deleteArticle(email, responseSingleArticle.getSlug());
//
//        Optional<Article> actualArticle = articleRepository.findBySlugOrderByIdDesc(responseSingleArticle.getSlug());
//        // then
//        assertThat(actualArticle).isEmpty();
//    }
//
//    @DisplayName("글 삭제 실패(존재하지 않는 글) 테스트")
//    @Test
//    void when_deleteArticle_expect_fail_exception() {
//        // given
//        String email = "seokrae@gmail.com";
//        User user = UserFactory.of(email, "1234", "seokrae");
//        RequestSaveArticle saveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", "Java");
//        // when
//        userRepository.save(user);
//        articleService.postArticle(email, saveArticle);
//
//        // then
//        assertThatExceptionOfType(NotFoundArticleException.class)
//                .isThrownBy(() -> articleService.deleteArticle(email, "illegal_slug"));
//    }
//
//    @DisplayName("글 조건 조회 및 페이징 테스트")
//    @Test
//    void when_searchPage_expect_success_condition() {
//        // given
//        getDummyArticles();
//        RequestArticleCondition requestCondition = RequestArticleCondition.of(null, "other@gmail.com", null);
//        PageRequest pageRequest = PageRequest.of(0, 20);
//        // when
//        ResponseMultiArticle responseMultiArticle = articleService.searchPageArticles(requestCondition, pageRequest);
//        int articleCount = responseMultiArticle.getArticleCount();
//
//        // then
//        assertThat(articleCount).isEqualTo(2);
//    }
//
//    private void getDummyArticles() {
//        User currentUser = userRepository.save(createUser("seokrae@gmail.com", "seokrae"));
//        User otherUser = userRepository.save(createUser("other@gmail.com", "other"));
//
//        List<RequestSaveArticle> currentUserArticles = List.of(
//                getRequestSaveArticle(1, "Java"),
//                getRequestSaveArticle(2, "JavaScript"),
//                getRequestSaveArticle(3, "JavaScript")
//        );
//
//        List<RequestSaveArticle> otherUserArticles = List.of(
//                getRequestSaveArticle(4, "Java"),
//                getRequestSaveArticle(5, "JavaScript")
//        );
//
//        List<Article> dummyCurrentUserArticles = currentUserArticles.stream()
//                .map(request -> RequestSaveArticle.toEntity(request, currentUser))
//                .collect(Collectors.toList());
//
//        List<Article> dummyOtherUserArticles = otherUserArticles.stream()
//                .map(request -> RequestSaveArticle.toEntity(request, otherUser))
//                .collect(Collectors.toList());
//
//        List<Article> dummyArticles = Stream.concat(dummyCurrentUserArticles.stream(), dummyOtherUserArticles.stream())
//                .collect(Collectors.toList());
//        articleRepository.saveAll(dummyArticles);
//    }
}