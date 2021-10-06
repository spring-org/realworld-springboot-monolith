package com.example.realworld.application.users.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static com.example.realworld.application.users.UserFixture.getRequestLoginUser;
import static com.example.realworld.application.users.UserFixture.getRequestSaveUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthApiTest extends BaseSpringBootTest {
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

    @DisplayName("사용자 등록 테스트")
    @Test
    void when_signUpUser_expect_success_new_user() throws Exception {
        // given
        RequestSaveUser requestSaveUser = getRequestSaveUser("seokrae@gmail.com");
        // then
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(requestSaveUser))
                )

                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @DisplayName("사용자 로그인 API 테스트")
    @Test
    void when_loginUser_expect_success_() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email);
        RequestLoginUser loginUser = getRequestLoginUser(email);
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seokrae"));
    }

    @DisplayName("사용자 로그아웃 테스트")
    @Test
    void when_logout_expect_success_session_remove() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email);
        RequestLoginUser loginUser = getRequestLoginUser(email);
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        delete("/api/users/logout")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )

                .andExpect(status().isNoContent());
    }

    @DisplayName("사용자 로그아웃 (실패) 테스트")
    @Test
    void when_logout_expect_success_fail_exception() throws Exception {
        // given
        RequestSaveUser saveUser = getRequestSaveUser("seokrae@gmail.com");
        // when
        userService.postUser(saveUser);
        // then
        mockMvc.perform(
                        delete("/api/users/logout").session(session)
                )

                .andExpect(status().isNoContent());
    }

}