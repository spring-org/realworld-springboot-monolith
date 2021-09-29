package com.example.realworld.application.users.presentation;

import com.example.realworld.application.ControllerTest;
import com.example.realworld.application.follows.exception.CannotFollowException;
import com.example.realworld.application.follows.service.FollowService;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.core.exception.UnauthorizedUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileApiTest extends ControllerTest {

    @Autowired
    private FollowService followService;

    @DisplayName("프로필 조회 테스트")
    @Test
    void when_getProfile_expect() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");

        // when
        userService.addUser(saveUser);

        // then
        mockMvc.perform(
                        get("/api/profiles/{toEmail}", email)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"));
    }

    @DisplayName("팔로우 성공 시 프로필 조회 테스트")
    @Test
    void when_postFollowUser_expect_success() throws Exception {
        // given
        RequestSaveUser fromUser = RequestSaveUser.of("seokrae@gmail.com", "seokrae", "1234");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = RequestSaveUser.of(toUserEmail, "seok", "1234");

        // when
        userService.addUser(fromUser);
        userService.addUser(toUser);

        // then
        mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(toUserEmail))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(true));
    }

    @DisplayName("팔로우 성공 시 프로필 조회 (권한 예외) 실패 테스트")
    @Test
    void when_postFollowUser_expect_fail_unAuthorize_exception() throws Exception {
        // given
        String fromUserEmail = "seokrae@gmail.com";
        RequestSaveUser fromUser = RequestSaveUser.of(fromUserEmail, "seokrae", "1234");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = RequestSaveUser.of(toUserEmail, "seok", "1234");
        session = new MockHttpSession();
        session.setAttribute("email", "");

        // when
        userService.addUser(fromUser);
        userService.addUser(toUser);

        // then
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", fromUserEmail)
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }

    @DisplayName("팔로우 성공 시 프로필 조회 (자기 자신을 팔로우 하려는 경우) 실패 테스트")
    @Test
    void when_postFollowUser_expect_fail_already_follow() throws Exception {
        // given
        String fromUserEmail = "seokrae@gmail.com";
        RequestSaveUser fromUser = RequestSaveUser.of(fromUserEmail, "seokrae", "1234");

        // when
        userService.addUser(fromUser);

        // then
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/profiles/{toEmail}/follow", fromUserEmail)
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(CannotFollowException.class);
    }

    @DisplayName("언팔로우 시 프로필 조회 테스트")
    @Test
    void when_deleteUnFollowUser_expect_success() throws Exception {
        // given
        RequestSaveUser fromUser = RequestSaveUser.of("seokrae@gmail.com", "seokrae", "1234");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = RequestSaveUser.of(toUserEmail, "seok", "1234");
        // when
        userService.addUser(fromUser);
        userService.addUser(toUser);

        followService.follow(toUserEmail, fromUser.getEmail());

        // then
        mockMvc.perform(
                        delete("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(toUserEmail))
                .andExpect(jsonPath("userName").value("seok"))
                .andExpect(jsonPath("following").value(false));
    }

    @DisplayName("언팔로우시 프로필 조회 실패(권한 예외) 테스트")
    @Test
    void when_deleteUnFollowUser_expect_fail_unAuthorize_exception() throws Exception {
        // given
        RequestSaveUser fromUser = RequestSaveUser.of("seokrae@gmail.com", "seokrae", "1234");
        String toUserEmail = "seok@gmail.com";
        RequestSaveUser toUser = RequestSaveUser.of(toUserEmail, "seok", "1234");

        session = new MockHttpSession();
        session.setAttribute("email", "");

        // when
        userService.addUser(fromUser);
        userService.addUser(toUser);

        // then
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/api/profiles/{toEmail}/follow", toUserEmail)
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }
}