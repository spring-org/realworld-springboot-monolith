package com.example.realworld.application;

import com.example.realworld.application.articles.dto.RequestArticleCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;
import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.example.realworld.application.articles.ArticleFixture.*;
import static com.example.realworld.application.users.UserFixture.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

// TODO 인증 관련 모킹(인가 부분)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AcceptanceTest {

    private static final String BEARER_PREFIX = "Bearer ";
    private String user1Token;
    private String user2Token;

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private int port;
    private long commentId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //serialize, deserialize
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(
                        new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> {
                            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
                            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
                            mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
                            return mapper;
                        })
                );
    }

    @Order(1)
    @DisplayName("사용자(user1) 등록 테스트")
    @Test
    void when_signUp_user1() throws JsonProcessingException {
        RequestSaveUser requestSaveUser = getRequestSaveUser("user1@gmail.com", "user1");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(requestSaveUser)) // jackson object mapper 가 꼭 필요한가?
                .when()
                .post("/api/users")
                .then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @Order(2)
    @DisplayName("사용자(user2) 등록 테스트")
    @Test
    void when_signUp_user2() throws JsonProcessingException {
        RequestSaveUser requestSaveUser = getRequestSaveUser("user2@gmail.com", "user2");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(requestSaveUser)) // jackson object mapper 가 꼭 필요한가?
                .when()
                .post("/api/users")
                .then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @Order(3)
    @DisplayName("사용자(user1) 로그인 테스트")
    @Test
    void when_login_user1() throws JsonProcessingException {
        RequestLoginUser requestLoginUser = getRequestLoginUser("user1@gmail.com");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(requestLoginUser))
                .when()
                .post("/api/users/login")
                .andReturn();

        response
                .then()
                .assertThat().statusCode(HttpStatus.OK.value());
        user1Token = response.header(AUTHORIZATION);
    }

    @Order(4)
    @DisplayName("사용자(user2) 로그인 테스트")
    @Test
    void when_login_user2() throws JsonProcessingException {
        RequestLoginUser requestLoginUser = getRequestLoginUser("user2@gmail.com");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(requestLoginUser))
                .when()
                .post("/api/users/login")
                .andReturn();

        response
                .then()
                .assertThat().statusCode(HttpStatus.OK.value());
        user2Token = response.header(AUTHORIZATION);
    }

    @Order(5)
    @DisplayName("특정 사용자(user1) 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_user1() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .get("/api/profiles/{toEmail}", "user1@gmail.com");

        response
                .then()
                .assertThat().statusCode(HttpStatus.OK.value())
                .body("email", is("user1@gmail.com"))
                .body("userName", is("user1"));
    }

    @Order(6)
    @DisplayName("특정 사용자 프로필 조회 (JWT 없이 조회) 테스트")
    @Test
    void when_getProfile_fail_jwt_exception() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/profiles/{toEmail}", "user2@gmail.com");

        response
                .then()
                .assertThat().statusCode(HttpStatus.OK.value())
                .body("email", is("user2@gmail.com"))
                .body("userName", is("user2"));
    }

    @Order(7)
    @DisplayName("사용자(user1) 조회 테스트")
    @Test
    void when_currentUser() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .get("/api/user");

        response
                .then()
                .assertThat().statusCode(HttpStatus.OK.value())
                .body("email", is("user1@gmail.com"))
                .body("userName", is("user1"));
    }

    @Order(8)
    @DisplayName("사용자 조회 권한 (예외) 테스트")
    @Test
    void when_currentUser_fail_unAuthorization() {
        Response response = given()
                .when()
                .get("/api/user");

        response.then()
                .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Order(9)
    @DisplayName("사용자(user1)의 정보 수정 테스트")
    @Test
    void when_putUser_expect_success_user1_change_info() throws JsonProcessingException {
        RequestUpdateUser updateUser = getRequestUpdateUser("updatedUser", "1234", "newImage.png", "newBio");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .body(mapper.writeValueAsString(updateUser))
                .when()
                .put("/api/user");

        response.then()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(10)
    @DisplayName("사용자의 정보를 수정 (JWT 인가 예외) 테스트")
    @Test
    void when_putUser_expect_fail_unAuthorization() throws JsonProcessingException {
        RequestUpdateUser updateUser = getRequestUpdateUser("updatedUser", "1234", "newImage.png", "newBio");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(updateUser))
                .when()
                .put("/api/user");

        response.then()
                .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Order(11)
    @DisplayName("팔로우(user1 -> user2) 성공 시 프로필 조회 테스트")
    @Test
    void when_followUser_expect_success() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .post("/api/profiles/{toEmail}/follow", "user2@gmail.com");
        response.then()
                .assertThat().statusCode(HttpStatus.OK.value())
                .body("email", is("user2@gmail.com"))
                .body("userName", is("user2"))
                .body("following", is(true));
    }

    @Order(12)
    @DisplayName("팔로우(user1 -> user2) 실패 예외 (권한 예외) 테스트")
    @Test
    void when_followUser_expect_fail_unAuthorization() {
        Response response = given()
                .when()
                .post("/api/profiles/{toEmail}/follow", "user2@gmail.com");
        response.then()
                .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Order(13)
    @DisplayName("사용자(user1)의 글 등록1 작성 테스트")
    @Test
    void when_post_by_seokRae_expect_success_article1() throws JsonProcessingException {
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle(1, "Java", "JavaScript");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .body(mapper.writeValueAsString(requestSaveArticle))
                .when()
                .post("/api/articles");

        response.then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @Order(14)
    @DisplayName("사용자(user2)의 글 등록2 작성 테스트")
    @Test
    void when_post_by_seokRae_expect_success_article2() throws JsonProcessingException {
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle(2, "Java", "JavaScript");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + user2Token)
                .body(mapper.writeValueAsString(requestSaveArticle))
                .when()
                .post("/api/articles");

        response.then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @Order(15)
    @DisplayName("사용자(user2)의 글 등록3 작성 테스트")
    @Test
    void when_post_by_seok_expect_success_article1() throws JsonProcessingException {
        RequestSaveArticle requestSaveArticle = getRequestSaveArticle(3, "Java", "JavaScript");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + user2Token)
                .body(mapper.writeValueAsString(requestSaveArticle))
                .when()
                .post("/api/articles");

        response.then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @Order(16)
    @DisplayName("특정 글 조회 테스트")
    @Test
    void when_getArticle_expect_success_article() {
        Response response = given()
                .when()
                .get("/api/articles/{slug}", makeSlug("타이틀-2"));

        response.then()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(17)
    @DisplayName("글 페이징 조건 조회 테스트")
    @Test
    void when_getArticles_expect_success_page_article() {
        RequestArticleCondition pageCondition =
                RequestArticleCondition.of("JavaScript", null, null);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pageCondition)
                .when()
                .get("/api/articles");

        response
                .then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(18)
    @DisplayName("피드 조회 테스트")
    @Test
    void when_getFeed_expect_success_following_article() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .body(pageRequest)
                .when()
                .get("/api/articles/feed");

        response
                .then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(19)
    @DisplayName("언팔로우 프로필 조회 테스트")
    @Test
    void when_unFollowUser_expect_success() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .delete("/api/profiles/{toEmail}/follow", "user2@gmail.com");

        response
                .then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(20)
    @DisplayName("사용자의 관심 글 등록 테스트")
    @Test
    void when_favoriteArticle_expect_success_favorite() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .post("/api/articles/{slug}/favorite", makeSlug("타이틀-2"));

        response
                .then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(21)
    @DisplayName("사용자의 관심 글 취소 테스트")
    @Test
    void when_unFavoriteArticle_expect_success_un_favorite() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .delete("/api/articles/{slug}/favorite", makeSlug("타이틀-2"));

        response
                .then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(22)
    @DisplayName("특정 글 수정 테스트")
    @Test
    void when_updateArticle_expect_success_updated_article() throws JsonProcessingException {
        RequestUpdateArticle updateArticle = getRequestUpdateArticle("수정된 타이틀", "설명 추가", "내용");

        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user2Token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(updateArticle))
                .when()
                .put("/api/articles/{slug}", makeSlug("타이틀-2"));

        response
                .then().assertThat().statusCode(HttpStatus.OK.value())
                .body("slug", is(makeSlug("수정된 타이틀")))
                .body("author.email", is("user2@gmail.com"));
    }

    @Order(23)
    @DisplayName("특정(본인) 글에 커멘트 등록 하는 테스트")
    @Test
    void when_post_comment_expect_success() throws JsonProcessingException {
        RequestSaveComment requestSaveComment = RequestSaveComment.from("글 좋아요 ~");

        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(requestSaveComment))
                .when()
                .post("/api/articles/{slug}/comments", makeSlug("타이틀-1"));

        response.then()
                .assertThat().statusCode(HttpStatus.CREATED.value());
        commentId = response.jsonPath().getLong("id");
    }

    @Order(24)
    @DisplayName("특정 글의 전체 커멘트 조회 테스트")
    @Test
    void when_get_comments_expect_success_comments() {
        Response response = given()
                .when()
                .get("/api/articles/{slug}/comments", makeSlug("타이틀-1"));

        response.then()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @Order(25)
    @DisplayName("커멘트 삭제 테스트")
    @Test
    void when_delete_comment_expect_success_delete_comment() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .delete("/api/articles/{slug}/comments/{commentId}", makeSlug("타이틀-1"), commentId);

        response.then()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(26)
    @DisplayName("특정 글 삭제 테스트")
    @Test
    void when_delete_article_expect_success() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user2Token)
                .when()
                .delete("/api/articles/{slug}", makeSlug("타이틀-3"));

        response.then()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(27)
    @DisplayName("사용자(user1) 로그아웃 테스트")
    @Test
    void when_logout_expect_success_user1_logout() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user1Token)
                .when()
                .delete("/api/users/logout");

        response.then()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(28)
    @DisplayName("사용자(user2) 로그아웃 테스트")
    @Test
    void when_logout_expect_success_user2_logout() {
        Response response = given()
                .header(AUTHORIZATION, BEARER_PREFIX + user2Token)
                .when()
                .delete("/api/users/logout");

        response.then()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
