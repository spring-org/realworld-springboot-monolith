package com.example.realworld.application.users.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiTest extends BaseSpringBootTest {
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("email", "seokrae@gmail.com");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("현재 사용자의 정보를 조회")
    @Test
    void when_getCurrentUser_expect_success_get_current_user() throws Exception {
        // given
        RequestSaveUser saveUser = getRequestSaveUser("seokrae@gmail.com");
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        get("/api/user")
                                .session(session)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @DisplayName("현재 사용자의 정보를 조회 실패 (빈 세션 권한 예외) 테스트")
    @Test
    void when_getCurrentUser_expect_fail_session_is_empty() throws Exception {
        // given
        RequestSaveUser saveUser = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        // when
        userService.postUser(saveUser);
        // then
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/user")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }

    @DisplayName("현재 사용자의 정보를 수정")
    @Test
    void when_putUser_expect_success_updated_user_info() throws Exception {
        // given
        RequestSaveUser saveUser = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        RequestUpdateUser updateUser = RequestUpdateUser.of(
                "seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        put("/api/user")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("updatedUser"))
                .andExpect(jsonPath("bio").value("newBio"))
                .andExpect(jsonPath("image").value("newImage.png"));
    }

    @DisplayName("현재 사용자의 정보를 수정 (빈 이메일 권한예외) 테스트")
    @Test
    void when_putUser_expect_fail_unAuthorized() throws Exception {

        // given
        RequestSaveUser saveUser = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        RequestUpdateUser updateUser = RequestUpdateUser.of(
                "seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        session = new MockHttpSession();
        session.setAttribute("email", "");

        // when
        userService.postUser(saveUser);
        // then
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/user")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }

    @DisplayName("현재 사용자의 정보를 수정 (잘못된 이메일로 권한예외) 테스트")
    @Test
    void when_putUser_expect_fail_un_matches_email_unAuthorized() throws Exception {
        session = new MockHttpSession();
        session.setAttribute("email", "seok@gmail.com");
        // given
        RequestSaveUser saveUser = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        RequestUpdateUser updateUser = RequestUpdateUser.of(
                "seokrae@gmail.com", "updatedUser", "1234", "newImage.png", "newBio");
        // when
        userService.postUser(saveUser);
        // then
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/user")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUser))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(UnauthorizedUserException.class);
    }
}