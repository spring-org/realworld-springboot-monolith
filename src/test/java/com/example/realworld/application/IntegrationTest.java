package com.example.realworld.application;

import com.example.realworld.application.articles.dto.RequestArticleCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;
import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static com.example.realworld.application.articles.ArticleFixture.*;
import static com.example.realworld.application.users.UserFixture.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REPLACEMENT_EMPTY_DELIMITER = "";
    private String sr_token;
    private String seok_token;
    private Long commentId;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;

    @Order(1)
    @DisplayName("사용자(SeokRae) 등록 테스트")
    @Test
    void when_signUpUser_expect_success_new_user() throws Exception {
        // given
        RequestSaveUser requestSaveUser = getRequestSaveUser("seokrae@gmail.com");
        // then
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(requestSaveUser))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @Order(2)
    @DisplayName("사용자(Seok) 등록 테스트")
    @Test
    void when_signUpUser_expect_success_other_user() throws Exception {
        // given
        RequestSaveUser requestSaveUser = getRequestSaveUser("seok@gmail.com", "seok");
        // then
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(requestSaveUser))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("seok@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"));
    }

    @Order(3)
    @DisplayName("사용자(SeokRae) 로그인 API 테스트")
    @Test
    void when_loginUser_expect_success_return_token() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestLoginUser loginUser = getRequestLoginUser(email);
        // when

        // then₩
        MockHttpServletResponse response = mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String header = response.getHeader(HttpHeaders.AUTHORIZATION);
        sr_token = Objects.requireNonNull(header).replace(BEARER_PREFIX, REPLACEMENT_EMPTY_DELIMITER);
    }

    @Order(4)
    @DisplayName("사용자(Seok) 로그인 API 테스트")
    @Test
    void when_loginUser_expect_success_other_user() throws Exception {
        String email = "seok@gmail.com";
        RequestLoginUser loginUser = getRequestLoginUser(email);
        MockHttpServletResponse response = mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String header = response.getHeader(HttpHeaders.AUTHORIZATION);
        seok_token = Objects.requireNonNull(header).replace(BEARER_PREFIX, REPLACEMENT_EMPTY_DELIMITER);
    }

    @Order(5)
    @DisplayName("프로필 조회 테스트")
    @Test
    void when_getProfile_expect() throws Exception {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        // when
        // then
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", currentUserEmail)
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @Order(6)
    @DisplayName("프로필 조회 (JWT 없이 요청) 테스트")
    @Test
    void when_getProfile_expect_success() throws Exception {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        // when
        // then
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", currentUserEmail)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.userName").value("seokrae"))
                .andExpect(jsonPath("$.following").value(false));
    }

    @Order(7)
    @DisplayName("프로필 조회 (현재 사용자 존재 시) 테스트")
    @Test
    void when_getProfile_expect_success_auth() throws Exception {
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", "seok@gmail.com")
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seok@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(false));
    }

    @Order(8)
    @DisplayName("현재 사용자의 정보를 조회")
    @Test
    void when_getCurrentUser_expect_success_get_current_user() throws Exception {
        mockMvc.perform(
                        get("/api/user")
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @Order(9)
    @DisplayName("현재 사용자의 정보를 조회 실패 (JWT 없이 요청) 테스트")
    @Test
    void when_getCurrentUser_expect_fail_session_is_empty() throws Exception {
        mockMvc.perform(
                        get("/api/user")
                )
                .andExpect(status().isUnauthorized());
    }

    @Order(10)
    @DisplayName("현재 사용자의 정보를 수정")
    @Test
    void when_putUser_expect_success_updated_user_info() throws Exception {
        RequestUpdateUser updateUser = getRequestUpdateUser("seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        mockMvc.perform(
                        put("/api/user")
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("updatedUser"))
                .andExpect(jsonPath("bio").value("newBio"))
                .andExpect(jsonPath("image").value("newImage.png"));
    }

    @Order(11)
    @DisplayName("현재 사용자의 정보를 수정 (빈 이메일 권한예외) 테스트")
    @Test
    void when_putUser_expect_fail_unAuthorized() throws Exception {
        RequestUpdateUser updateUser = getRequestUpdateUser("seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        mockMvc.perform(
                        put("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andExpect(status().isUnauthorized());
    }

    @Order(12)
    @DisplayName("현재 사용자의 정보를 수정 (잘못된 이메일로 권한예외) 테스트")
    @Test
    void when_putUser_expect_fail_un_matches_email_unAuthorized() throws Exception {
        RequestUpdateUser updateUser = getRequestUpdateUser("seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        mockMvc.perform(
                        put("/api/user")
                                .header(AUTHORIZATION, BEARER_PREFIX + seok_token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andExpect(status().isUnauthorized());
    }

    @Order(13)
    @DisplayName("팔로우 성공 시 프로필 조회 테스트")
    @Test
    void when_postFollowUser_expect_success() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", "seok@gmail.com")
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seok@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(true));
    }

    @Order(14)
    @DisplayName("팔로우 성공 시 프로필 조회 (권한 예외) 실패 테스트")
    @Test
    void when_postFollowUser_expect_fail_unAuthorize_exception() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", "seok@gmail.com")
                )
                .andExpect(status().isUnauthorized());
    }

    @Order(15)
    @DisplayName("사용자(SeokRae)의 글 작성 테스트")
    @Test
    void when_postArticle_expect_success_new_article() throws Exception {
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle(1, "Java", "JavaScript");
        mockMvc.perform(
                        post("/api/articles")
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀-1")))
                .andExpect(jsonPath("$.title").value("타이틀-1"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.tagList.[1]").value("JavaScript"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("updatedUser"))
                .andExpect(jsonPath("$.author.following").value(false));
    }

    @Order(16)
    @DisplayName("사용자(seok)의 글 작성 테스트")
    @Test
    void when_postArticle_expect_success_new_article2() throws Exception {
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle(2, "Java", "Spring");
        mockMvc.perform(
                        post("/api/articles")
                                .header(AUTHORIZATION, BEARER_PREFIX + seok_token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀-2")))
                .andExpect(jsonPath("$.title").value("타이틀-2"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.tagList.[1]").value("Spring"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.author.email").value("seok@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("seok"))
                .andExpect(jsonPath("$.author.following").value(true));
    }

    @Order(17)
    @DisplayName("특정 글 조회 테스트")
    @Test
    void when_getArticle_expect_success_article() throws Exception {
        mockMvc.perform(
                        get("/api/articles/{slug}", makeSlug("타이틀-1"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀-1")))
                .andExpect(jsonPath("$.title").value("타이틀-1"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("updatedUser"))
                .andExpect(jsonPath("$.author.following").value(false));
    }

    @Order(18)
    @DisplayName("글 페이징 조건 조회")
    @Test
    void when_getArticles_expect_success_page_article() throws Exception {
        RequestArticleCondition pageCondition =
                RequestArticleCondition.of("Java", null, null);
        mockMvc.perform(
                        get("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageCondition))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(19)
    @DisplayName("피드 조회 테스트")
    @Test
    void when_getPeed_expect_success_following_article() throws Exception {
        // user given
        PageRequest pageRequest = PageRequest.of(0, 20);
        // then
        mockMvc.perform(
                        get("/api/articles/feed")
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(mapper.writeValueAsString(pageRequest))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(20)
    @DisplayName("언팔로우 시 프로필 조회 테스트")
    @Test
    void when_deleteUnFollowUser_expect_success() throws Exception {
        // given
        String toUserEmail = "seok@gmail.com";
        // when
        // then
        mockMvc.perform(
                        delete("/api/profiles/{toEmail}/follow", toUserEmail)
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(toUserEmail))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(false));
    }

    @Order(21)
    @DisplayName("관심 글 등록 테스트")
    @Test
    void when_favoriteArticle_expect_success_favorite() throws Exception {
        mockMvc.perform(
                        post("/api/articles/{slug}/favorite", makeSlug("타이틀-2"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀-2")))
                .andExpect(jsonPath("$.title").value("타이틀-2"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.tagList.[1]").value("Spring"))
                .andExpect(jsonPath("$.favorited").value(true))
                .andExpect(jsonPath("$.favoritesCount").value(1))
                .andExpect(jsonPath("$.author.email").value("seok@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("seok"))
                .andExpect(jsonPath("$.author.following").value(false));
    }

    @Order(22)
    @DisplayName("관심 글 취소")
    @Test
    void when_unFavoriteArticle_expect_success_un_favorite() throws Exception {
        mockMvc.perform(
                        delete("/api/articles/{slug}/favorite", makeSlug("타이틀-2"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(makeSlug("타이틀-2")))
                .andExpect(jsonPath("$.title").value("타이틀-2"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.tagList.[1]").value("Spring"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.author.email").value("seok@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("seok"))
                .andExpect(jsonPath("$.author.following").value(false));
    }

    @Order(23)
    @DisplayName("특정 글 수정 테스트")
    @Test
    void when_updateArticle_expect_success_updated_article() throws Exception {
        RequestUpdateArticle updateArticle = getRequestUpdateArticle("수정된 타이틀", "설명 추가", "내용");
        mockMvc.perform(
                        put("/api/articles/{slug}", makeSlug("타이틀-1"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(updateArticle))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(makeSlug("수정된 타이틀")))
                .andExpect(jsonPath("$.title").value("수정된 타이틀"))
                .andExpect(jsonPath("$.description").value("설명 추가"))
                .andExpect(jsonPath("$.body").value("내용"))
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0))
                .andExpect(jsonPath("$.author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("$.author.userName").value("updatedUser"))
                .andExpect(jsonPath("$.author.following").value(false));
    }

    @Order(24)
    @DisplayName("특정(본인) 글에 커멘트 등록하는 테스트")
    @Test
    void when_addCommentsToArticle_expect_success_comments() throws Exception {
        // given
        RequestSaveComment requestSaveComment = RequestSaveComment.from("글 좋아요 ~");
        // when
        // then
        MockHttpServletResponse response = mockMvc.perform(
                        post("/api/articles/{slug}/comments", makeSlug("수정된 타이틀"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(requestSaveComment))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("body").value("글 좋아요 ~"))
                .andExpect(jsonPath("author.email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("author.userName").value("updatedUser"))
                .andReturn().getResponse();

        String contentAsString = response.getContentAsString();
        commentId = mapper.readTree(contentAsString).get("id").asLong();
    }

    @Order(25)
    @DisplayName("특정 글의 모든 커멘트 조회 테스트")
    @Test
    void when_getCommentsFromAnArticle_expect_success_comments() throws Exception {
        mockMvc.perform(
                        get("/api/articles/{slug}/comments", makeSlug("수정된 타이틀"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                // comments 사이즈 확인
                .andExpect(jsonPath("$.commentSize").value(1));
    }

    @Order(26)
    @DisplayName("커멘트 삭제 테스트")
    @Test
    void when_deleteComments_expect_success_delete_comment() throws Exception {
        mockMvc.perform(
                        delete("/api/articles/{slug}/comments/{id}", makeSlug("수정된 타이틀"), commentId)
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isNoContent());
    }


    @Order(27)
    @DisplayName("특정 글 삭제 테스트")
    @Test
    void when_deleteArticle_expect_success_deleted_article() throws Exception {
        mockMvc.perform(
                        delete("/api/articles/{slug}", makeSlug("수정된 타이틀"))
                                .header(AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isNoContent());
    }

    @Order(28)
    @DisplayName("사용자(seokrae) 로그아웃 테스트")
    @Test
    void when_logout_expect_success_seokrae_logout() throws Exception {
        mockMvc.perform(
                        delete("/api/users/logout")
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isNoContent());
    }

    @Order(29)
    @DisplayName("사용자(seok) 로그아웃 테스트")
    @Test
    void when_logout_expect_success_seok_logout() throws Exception {
        mockMvc.perform(
                        delete("/api/users/logout")
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + sr_token)
                )
                .andExpect(status().isNoContent());
    }
}
