package com.example.realworld.application.users.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.follows.service.FollowService;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.exception.UnauthorizedUserException;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.realworld.application.users.UserFixture.getRequestSaveUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileApiTest extends BaseSpringBootTest {
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private FollowService followService;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("email", "seokrae@gmail.com");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("프로필 조회 테스트")
    @Test
    void when_getProfile_expect() throws Exception {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(currentUserEmail);
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", currentUserEmail)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @DisplayName("프로필 조회 (현재 사용자 존재 시) 테스트")
    @Test
    void when_getProfile_expect_success_auth() throws Exception {
        // given
        RequestSaveUser currentUser = getRequestSaveUser("seokrae@gmail.com");
        String otherUserEmail = "other@gmail.com";
        RequestSaveUser otherUser = getRequestSaveUser(otherUserEmail, "other");
        // when
        userService.postUser(currentUser);
        userService.postUser(otherUser);
        // then
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", otherUserEmail)
                                .session(session)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("other@gmail.com"))
                .andExpect(jsonPath("userName").value("other"))
                .andExpect(jsonPath("following").value(false));
    }

    @DisplayName("팔로우 성공 시 프로필 조회 테스트")
    @Test
    void when_postFollowUser_expect_success() throws Exception {
        // given
        RequestSaveUser currentUser = getRequestSaveUser("seokrae@gmail.com");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = getRequestSaveUser(toUserEmail, "seok");
        // when
        userService.postUser(currentUser);
        userService.postUser(toUser);
        // then
        mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(toUserEmail))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(true));
    }

    @DisplayName("팔로우 성공 시 프로필 조회 (권한 예외) 실패 테스트")
    @Test
    void when_postFollowUser_expect_fail_unAuthorize_exception() throws Exception {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        RequestSaveUser currentUser = getRequestSaveUser(currentUserEmail);
        RequestSaveUser toUser = getRequestSaveUser("seok@gmail.com", "seok");
        session = new MockHttpSession();
        session.setAttribute("email", "");
        // when
        userService.postUser(currentUser);
        userService.postUser(toUser);
        // then
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", currentUserEmail)
                                .session(session)
                )

                .andExpect(status().isUnauthorized())
                .andReturn();
        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }

    @DisplayName("언팔로우 시 프로필 조회 테스트")
    @Test
    void when_deleteUnFollowUser_expect_success() throws Exception {
        // given
        RequestSaveUser currentUser = getRequestSaveUser("seokrae@gmail.com");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = getRequestSaveUser(toUserEmail, "seok");
        // when
        userService.postUser(currentUser);
        userService.postUser(toUser);

        followService.follow(currentUser.getEmail(), toUserEmail);
        // then
        mockMvc.perform(
                        delete("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(toUserEmail))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(false));
    }

    @DisplayName("언팔로우시 프로필 조회 실패(권한 예외) 테스트")
    @Test
    void when_deleteUnFollowUser_expect_fail_unAuthorize_exception() throws Exception {
        // given
        RequestSaveUser currentUser = getRequestSaveUser("seokrae@gmail.com");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = getRequestSaveUser(toUserEmail, "seok");
        session = new MockHttpSession();
        session.setAttribute("email", "");
        // when
        userService.postUser(currentUser);
        userService.postUser(toUser);
        // then
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                )

                .andExpect(status().isUnauthorized())
                .andReturn();
        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }
}