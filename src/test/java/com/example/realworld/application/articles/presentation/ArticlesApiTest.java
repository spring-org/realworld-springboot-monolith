package com.example.realworld.application.articles.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.articles.persistence.repository.CommentRepository;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.articles.service.CommentService;
import com.example.realworld.application.favorites.persistence.repository.FavoriteArticleRepository;
import com.example.realworld.application.favorites.service.FavoriteArticleService;
import com.example.realworld.application.tags.persistence.TagType;
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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticlesApiTest extends BaseSpringBootTest {
    @Autowired
    protected UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FavoriteArticleService favoriteArticleService;
    @Autowired
    private FavoriteArticleRepository favoriteArticleRepository;

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
        userRepository.deleteAll();
    }

    @DisplayName("현재 사용자의 글 작성 테스트")
    @Test
    void when_postArticle_expect_success_new_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

        // when
        userService.postUser(saveUser);

        mockMvc.perform(
                        post("/api/articles")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("title").value("타이틀"))
                .andExpect(jsonPath("description").value("설명"))
                .andExpect(jsonPath("body").value("내용"))
//                .andExpect(jsonPath("tagList", 0).value("java"))
                .andExpect(jsonPath("favorited").value(false))
                .andExpect(jsonPath("favoritesCount").value(0))
                .andExpect(jsonPath("user.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("user.userName").value("seok"))
                .andExpect(jsonPath("user.following").value(false));
    }

    @DisplayName("특정 글 조회 테스트")
    @Test
    void when_getArticle_expect_success_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

        // when
        userService.postUser(saveUser);
        ResponseSingleArticle responseSingleArticle = articleService.postArticle(email, requestSaveArticle);

        // then
        mockMvc.perform(
                        get("/api/articles/{slug}", responseSingleArticle.getSlug())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("title").value("타이틀"))
                .andExpect(jsonPath("description").value("설명"))
                .andExpect(jsonPath("body").value("내용"))
//                .andExpect(jsonPath("tagList").value(new String[]{"java"}))
                .andExpect(jsonPath("favorited").value(false))
                .andExpect(jsonPath("favoritesCount").value(0))
                .andExpect(jsonPath("user.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("user.userName").value("seok"))
                .andExpect(jsonPath("user.following").value(false));

    }

    @DisplayName("특정 글 수정 테스트")
    @Test
    void when_updateArticle_expect_success_updated_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));
        RequestUpdateArticle updateArticle = RequestUpdateArticle.of("수정된 타이틀", "설명 추가", "내용");
        // when
        userService.postUser(saveUser);
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
                .andExpect(jsonPath("slug").value(makeSlug("수정된 타이틀")))
                .andExpect(jsonPath("title").value("수정된 타이틀"))
                .andExpect(jsonPath("description").value("설명 추가"))
                .andExpect(jsonPath("body").value("내용"))
//                .andExpect(jsonPath("tagList").value(new String[]{"java"}))
                .andExpect(jsonPath("favorited").value(false))
                .andExpect(jsonPath("favoritesCount").value(0))
                .andExpect(jsonPath("user.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("user.userName").value("seok"))
                .andExpect(jsonPath("user.following").value(false));
    }

    @DisplayName("특정 글 삭제 테스트")
    @Test
    void when_deleteArticle_expect_success_deleted_article() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

        // when
        userService.postUser(saveUser);
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
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));
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
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("body").value("글 좋아요 ~"))
                .andExpect(jsonPath("author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("author.userName").value("seok"));
    }

    @DisplayName("특정 글의 모든 커멘트 조회 테스트")
    @Test
    void when_getCommentsFromAnArticle_expect_success_comments() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));
        RequestSaveComment requestSaveComment1 = RequestSaveComment.from("1. 퍼가요 ~ ");
        RequestSaveComment requestSaveComment2 = RequestSaveComment.from("2. 퍼가요 ~ ");
        RequestSaveComment requestSaveComment3 = RequestSaveComment.from("3. 퍼가요 ~ ");

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
                .andExpect(jsonPath("$.commentSize").value(3));
    }

    @DisplayName("커멘트 삭제 테스트")
    @Test
    void when_deleteComments_expect_success_delete_comment() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));
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
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = RequestSaveUser.of(otherUserEmail, "otherSeok", "1234");
        // article given
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

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
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.title").value("타이틀"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
//                .andExpect(jsonPath("tagList").value("내용"))
                .andExpect(jsonPath("$.favorited").value(true))
                .andExpect(jsonPath("$.favoritesCount").value(1))
                .andExpect(jsonPath("$.user.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.user.userName").value("otherSeok"))
                .andExpect(jsonPath("$.user.following").value(false));
    }

    @DisplayName("관심 글 취소")
    @Test
    void when_unFavoriteArticle_expect_success_un_favorite() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = RequestSaveUser.of(otherUserEmail, "otherSeok", "1234");
        // article given
        RequestSaveArticle requestSaveArticle = RequestSaveArticle.of("타이틀", "설명", "내용", Set.of(TagType.JAVA));

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
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀")))
                .andExpect(jsonPath("$.title").value("타이틀"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
//                .andExpect(jsonPath("tagList").value("내용"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.user.email").value("other@gmail.com"))
                .andExpect(jsonPath("$.user.userName").value("otherSeok"))
                .andExpect(jsonPath("$.user.following").value(false));
    }

    @DisplayName("글 페이징 조회")
    @Test
    void when_getArticles_expect_success_page_article() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = RequestSaveUser.of(otherUserEmail, "otherSeok", "1234");
        // article given
        RequestSaveArticle requestSaveArticle1 = RequestSaveArticle.of("타이틀1", "설명", "내용", Set.of(TagType.JAVA));
        RequestSaveArticle requestSaveArticle2 = RequestSaveArticle.of("타이틀2", "설명", "내용", Set.of(TagType.JAVA));
        RequestSaveArticle requestSaveArticle3 = RequestSaveArticle.of("타이틀3", "설명", "내용", Set.of(TagType.PERL));
        RequestSaveArticle requestSaveArticle4 = RequestSaveArticle.of("타이틀4", "설명", "내용", Set.of(TagType.PYTHON));
        RequestSaveArticle requestSaveArticle5 = RequestSaveArticle.of("타이틀5", "설명", "내용", Set.of(TagType.JAVASCRIPT));
        RequestPageCondition pageCondition =
                RequestPageCondition.of(TagType.JAVASCRIPT.tagName(), "other@gmail.com", "", 20, 0);
        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);

        articleService.postArticle(email, requestSaveArticle1);
        articleService.postArticle(email, requestSaveArticle2);
        articleService.postArticle(otherUserEmail, requestSaveArticle3);
        articleService.postArticle(otherUserEmail, requestSaveArticle4);
        articleService.postArticle(otherUserEmail, requestSaveArticle5);

        // then
        mockMvc.perform(
                        get("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageCondition))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleCount").value(1));
    }

    @DisplayName("피드 조회 테스트")
    @Test
    void when_getPeed_expect_success_following_aritcle() throws Exception {
        // user given
        String email = "seokrae@gmail.com";
        RequestSaveUser requestSaveUser = RequestSaveUser.of(email, "seok", "1234");
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser requestSaveOtherUser = RequestSaveUser.of(otherUserEmail, "otherSeok", "1234");
        String anotherUserEmail = "another@gmail.com";
        RequestSaveUser requestSaveAnotherUser = RequestSaveUser.of(anotherUserEmail, "anotherSeok", "1234");
        // article given
        RequestSaveArticle requestSaveArticle1 = RequestSaveArticle.of("타이틀1", "설명", "내용", Set.of(TagType.JAVA));
        RequestSaveArticle requestSaveArticle2 = RequestSaveArticle.of("타이틀2", "설명", "내용", Set.of(TagType.JAVA));
        RequestSaveArticle requestSaveArticle3 = RequestSaveArticle.of("타이틀3", "설명", "내용", Set.of(TagType.PYTHON));
        RequestSaveArticle requestSaveArticle4 = RequestSaveArticle.of("타이틀4", "설명", "내용", Set.of(TagType.R));
        RequestSaveArticle requestSaveArticle5 = RequestSaveArticle.of("타이틀5", "설명", "내용", Set.of(TagType.JAVASCRIPT));
        RequestPageCondition pageCondition =
                RequestPageCondition.of(TagType.JAVA.tagName(), "other@gmail.com", "", 20, 0);

        PageRequest pageRequest = PageRequest.of(0, 20);

        // when
        userService.postUser(requestSaveUser);
        userService.postUser(requestSaveOtherUser);
        userService.postUser(requestSaveAnotherUser);

        String slug1 = articleService.postArticle(otherUserEmail, requestSaveArticle1).getSlug();
        String slug2 = articleService.postArticle(otherUserEmail, requestSaveArticle2).getSlug();
        String slug3 = articleService.postArticle(anotherUserEmail, requestSaveArticle3).getSlug();
        String slug4 = articleService.postArticle(anotherUserEmail, requestSaveArticle4).getSlug();
        String slug5 = articleService.postArticle(anotherUserEmail, requestSaveArticle5).getSlug();

        favoriteArticleService.favoriteArticle(email, slug1);
        favoriteArticleService.favoriteArticle(email, slug2);
        favoriteArticleService.favoriteArticle(email, slug3);
        favoriteArticleService.favoriteArticle(email, slug4);
        favoriteArticleService.favoriteArticle(email, slug5);

        // then
        mockMvc.perform(
                        get("/api/articles/feed")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageRequest))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("모든 태그타입 조회 테스트")
    @Test
    void when_getTags_expect_success_all_data() throws Exception {
        mockMvc.perform(
                        get("/api/tags")
                )
                .andDo(print())
                .andExpect(status().isOk());

    }
}