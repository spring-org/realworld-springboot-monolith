package com.example.realworld.application.articles.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.articles.persistence.repository.CommentRepository;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.articles.service.CommentService;
import com.example.realworld.application.favorites.persistence.repository.FavoriteArticleRepository;
import com.example.realworld.application.favorites.service.FavoriteArticleService;
import com.example.realworld.application.follows.persistence.repository.FollowRepository;
import com.example.realworld.application.follows.service.FollowService;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static com.example.realworld.application.articles.ArticleFixture.getRequestSaveArticle;
import static com.example.realworld.application.articles.ArticleFixture.getRequestUpdateArticle;
import static com.example.realworld.application.users.UserFixture.getRequestSaveUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticlesApiTest extends BaseSpringBootTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FavoriteArticleService favoriteArticleService;
    @Autowired
    private FavoriteArticleRepository favoriteArticleRepository;

    @Autowired
    private FollowService followService;
    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("email", "seokrae@gmail.com");
    }

    @AfterEach
    void tearDown() {
        favoriteArticleRepository.deleteAll();
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        followRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("현재 사용자의 글 작성 테스트")
    @Test
    void when_postArticle_expect_success_new_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java", "JavaScript"});

        // when
        userService.postUser(requestSaveUser);

        mockMvc.perform(
                        post("/api/articles")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.article.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.article.title").value("타이틀"))
                .andExpect(jsonPath("$.article.description").value("설명"))
                .andExpect(jsonPath("$.article.body").value("내용"))
                .andExpect(jsonPath("$.article.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.article.tagList.[1]").value("JavaScript"))
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(0))
                .andExpect(jsonPath("$.article.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.article.author.userName").value("seokrae"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @DisplayName("특정 글 조회 테스트")
    @Test
    void when_getArticle_expect_success_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});

        // when
        userService.postUser(requestSaveUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, requestSaveArticle);

        // then
        mockMvc.perform(
                        get("/api/articles/{slug}", responseSingleArticle.getSlug())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.article.title").value("타이틀"))
                .andExpect(jsonPath("$.article.description").value("설명"))
                .andExpect(jsonPath("$.article.body").value("내용"))
                .andExpect(jsonPath("$.article.tagList", 0).value("Java"))
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(0))
                .andExpect(jsonPath("$.article.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.article.author.userName").value("seokrae"))
                .andExpect(jsonPath("$.article.author.following").value(false));

    }

    @DisplayName("특정 글 수정 테스트")
    @Test
    void when_updateArticle_expect_success_updated_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});
        RequestUpdateArticle updateArticle = getRequestUpdateArticle("수정된 타이틀", "설명 추가");
        // when
        userService.postUser(requestSaveUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, requestSaveArticle);

        // then
        mockMvc.perform(
                        put("/api/articles/{slug}", responseSingleArticle.getSlug())
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(updateArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(makeSlug("수정된 타이틀")))
                .andExpect(jsonPath("$.article.title").value("수정된 타이틀"))
                .andExpect(jsonPath("$.article.description").value("설명 추가"))
                .andExpect(jsonPath("$.article.body").value("내용"))
                .andExpect(jsonPath("$.article.tagList", 0).value("Java"))
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(0))
                .andExpect(jsonPath("$.article.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.article.author.userName").value("seokrae"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @DisplayName("특정 글 삭제 테스트")
    @Test
    void when_deleteArticle_expect_success_deleted_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});

        // when
        userService.postUser(requestSaveUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, requestSaveArticle);

        // then
        mockMvc.perform(
                        delete("/api/articles/{slug}", responseSingleArticle.getSlug())
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("특정(본인) 글에 커멘트 등록하는 테스트")
    @Test
    void when_addCommentsToArticle_expect_success_comments() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});
        RequestSaveComment requestSaveComment = RequestSaveComment.from("글 좋아요 ~");

        // when
        userService.postUser(requestSaveUser);
        String slug = articleService.postArticle(email, requestSaveArticle).getSlug();

        // then
        mockMvc.perform(
                        post("/api/articles/{slug}/comments", slug)
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveComment))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment.id").exists())
                .andExpect(jsonPath("$.comment.body").value("글 좋아요 ~"))
                .andExpect(jsonPath("$.comment.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.comment.author.userName").value("seokrae"));
    }

    @DisplayName("특정 글의 모든 커멘트 조회 테스트")
    @Test
    void when_getCommentsFromAnArticle_expect_success_comments() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});
        RequestSaveComment requestSaveComment1 = RequestSaveComment.from("퍼가요 ~ ");
        RequestSaveComment requestSaveComment2 = RequestSaveComment.from("퍼가요 ~ ");
        RequestSaveComment requestSaveComment3 = RequestSaveComment.from("퍼가요 ~ ");
        // when
        userService.postUser(requestSaveUser);
        String slug = articleService.postArticle(email, requestSaveArticle).getSlug();
        commentService.postComment(email, slug, requestSaveComment1);
        commentService.postComment(email, slug, requestSaveComment2);
        commentService.postComment(email, slug, requestSaveComment3);
        // then
        mockMvc.perform(
                        get("/api/articles/{slug}/comments", slug)
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                // comments 사이즈 확인
                .andExpect(jsonPath("$.comments.[0].comment.body").value("퍼가요 ~ "))
                .andExpect(jsonPath("$.comments.[0].comment.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.comments.[0].comment.author.userName").value("seokrae"))
                .andExpect(jsonPath("$.comments.[0].comment.author.following").value(false))
                .andExpect(jsonPath("$.commentSize").value(3));
    }

    @DisplayName("커멘트 삭제 테스트")
    @Test
    void when_deleteComments_expect_success_delete_comment() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});
        RequestSaveComment requestSaveComment1 = RequestSaveComment.from("1. 퍼가요 ~ ");
        RequestSaveComment requestSaveComment2 = RequestSaveComment.from("2. 퍼가요 ~ ");
        RequestSaveComment requestSaveComment3 = RequestSaveComment.from("3. 퍼가요 ~ ");

        // when
        userService.postUser(requestSaveUser);
        String slug = articleService.postArticle(email, requestSaveArticle).getSlug();
        commentService.postComment(email, slug, requestSaveComment1);
        Long commentId = commentService.postComment(email, slug, requestSaveComment2).getId();
        commentService.postComment(email, slug, requestSaveComment3);

        // then
        mockMvc.perform(
                        delete("/api/articles/{slug}/comments/{id}", slug, commentId)
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("관심 글 등록 테스트")
    @Test
    void when_favoriteArticle_expect_success_favorite() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "other");
        // article given
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});

        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);
        String slug = articleService.postArticle(otherUserEmail, requestSaveArticle).getSlug();

        // then
        mockMvc.perform(
                        post("/api/articles/{slug}/favorite", slug)
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.article.title").value("타이틀"))
                .andExpect(jsonPath("$.article.description").value("설명"))
                .andExpect(jsonPath("$.article.body").value("내용"))
                .andExpect(jsonPath("$.article.tagList", 0).value("Java"))
                .andExpect(jsonPath("$.article.favorited").value(true))
                .andExpect(jsonPath("$.article.favoritesCount").value(1))
                .andExpect(jsonPath("$.article.author.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.article.author.userName").value("other"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @DisplayName("관심 글 취소")
    @Test
    void when_unFavoriteArticle_expect_success_un_favorite() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "other");
        // article given
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle("타이틀", new String[]{"Java"});

        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);
        String slug = articleService.postArticle(otherUserEmail, requestSaveArticle).getSlug();
        favoriteArticleService.favoriteArticle(email, slug);

        // then
        mockMvc.perform(
                        delete("/api/articles/{slug}/favorite", slug)
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.article.title").value("타이틀"))
                .andExpect(jsonPath("$.article.description").value("설명"))
                .andExpect(jsonPath("$.article.body").value("내용"))
                .andExpect(jsonPath("$.article.tagList", 0).value("Java"))
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(0))
                .andExpect(jsonPath("$.article.author.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.article.author.userName").value("other"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @DisplayName("글 페이징 조건 조회")
    @Test
    void when_getArticles_expect_success_page_article() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "otherSeok");
        // article given
        RequestSaveArticle requestSaveArticle1 = getRequestSaveArticle("타이틀1", new String[]{"Java"});
        RequestSaveArticle requestSaveArticle2 = getRequestSaveArticle("타이틀2", new String[]{"Python"});
        RequestSaveArticle requestSaveArticle3 = getRequestSaveArticle("타이틀3", new String[]{"JavaScript"});
        RequestSaveArticle requestSaveArticle4 = getRequestSaveArticle("타이틀4", new String[]{"Java"});
        RequestSaveArticle requestSaveArticle5 = getRequestSaveArticle("타이틀5", new String[]{"JavaScript"});
        RequestArticleCondition pageCondition =
                RequestArticleCondition.of("JavaScript", "other@gmail.com", null);
        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);

        articleService.postArticle(email, requestSaveArticle1);
        articleService.postArticle(email, requestSaveArticle2);
        ResponseSingleArticle responseSingleArticle3 = articleService.postArticle(otherUserEmail, requestSaveArticle3);
        articleService.postArticle(otherUserEmail, requestSaveArticle4);
        ResponseSingleArticle responseSingleArticle5 = articleService.postArticle(otherUserEmail, requestSaveArticle5);

        // then
        mockMvc.perform(
                        get("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageCondition))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles.[0].article.slug").value(responseSingleArticle5.getSlug()))
                .andExpect(jsonPath("$.articles.[0].article.title").value(responseSingleArticle5.getTitle()))
                .andExpect(jsonPath("$.articles.[0].article.description").value(responseSingleArticle5.getDescription()))
                .andExpect(jsonPath("$.articles.[0].article.body").value(responseSingleArticle5.getBody()))
                .andExpect(jsonPath("$.articles.[0].article.tagList.[0]").value("JavaScript"))
                .andExpect(jsonPath("$.articles.[0].article.createdAt").exists())
                .andExpect(jsonPath("$.articles.[0].article.updatedAt").exists())
                .andExpect(jsonPath("$.articles.[0].article.favorited").value(false))
                .andExpect(jsonPath("$.articles.[0].article.favoritesCount").value(0))
                .andExpect(jsonPath("$.articles.[0].article.author.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.articles.[0].article.author.userName").value("otherSeok"))
                .andExpect(jsonPath("$.articles.[0].article.author.following").value(false))
                .andExpect(jsonPath("$.articles.[1].article.slug").value(responseSingleArticle3.getSlug()))
                .andExpect(jsonPath("$.articles.[1].article.title").value(responseSingleArticle3.getTitle()))
                .andExpect(jsonPath("$.articles.[1].article.description").value(responseSingleArticle3.getDescription()))
                .andExpect(jsonPath("$.articles.[1].article.body").value(responseSingleArticle3.getBody()))
                .andExpect(jsonPath("$.articleCount").value(2));
    }

    @DisplayName("피드 조회 테스트")
    @Test
    void when_getPeed_expect_success_following_article() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "otherSeok");
        String anotherUserEmail = "another@gmail.com";
        RequestSaveUser requestSaveAnotherUser = getRequestSaveUser(anotherUserEmail, "anotherSeok");
        // article given
        RequestSaveArticle requestSaveArticle1 = getRequestSaveArticle("타이틀1", new String[]{"Java"});
        RequestSaveArticle requestSaveArticle2 = getRequestSaveArticle("타이틀2", new String[]{"Java"});
        RequestSaveArticle requestSaveArticle3 = getRequestSaveArticle("타이틀3", new String[]{"Python"});
        RequestSaveArticle requestSaveArticle4 = getRequestSaveArticle("타이틀4", new String[]{"R"});
        RequestSaveArticle requestSaveArticle5 = getRequestSaveArticle("타이틀5", new String[]{"JavaScript"});

        PageRequest pageRequest = PageRequest.of(0, 20);

        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);
        userService.postUser(requestSaveAnotherUser);

        articleService.postArticle(otherUserEmail, requestSaveArticle1);
        articleService.postArticle(otherUserEmail, requestSaveArticle2);
        articleService.postArticle(anotherUserEmail, requestSaveArticle3);
        articleService.postArticle(anotherUserEmail, requestSaveArticle4);
        articleService.postArticle(anotherUserEmail, requestSaveArticle5);

        followService.follow(email, otherUserEmail);
        followService.follow(email, anotherUserEmail);

        // then
        mockMvc.perform(
                        get("/api/articles/feed")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageRequest))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles.*").isNotEmpty())
                .andExpect(jsonPath("$.articleCount").value(5));
    }
}