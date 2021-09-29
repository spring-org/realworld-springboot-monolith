package com.example.realworld.application.users.presentation;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    protected MockHttpSession session;

    @Autowired
    private ObjectMapper mapper;

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
        RequestSaveUser seok = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");

        // then
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(seok))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"));
    }

    @DisplayName("사용자 로그인 API 테스트")
    @Test
    void when_loginUser_expect_success_() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestLoginUser loginUser = RequestLoginUser.of(email, "1234");

        // when
        userService.addUser(saveUser);

        // then
        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("seokrae@gmail.com"))
                .andExpect(jsonPath("userName").value("seok"));
    }

    @DisplayName("사용자 로그아웃 테스트")
    @Test
    void when_logout_expect_success_session_remove() throws Exception {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = RequestSaveUser.of(email, "seok", "1234");
        RequestLoginUser loginUser = RequestLoginUser.of(email, "1234");

        // when
        userService.addUser(saveUser);

        // then
        mockMvc.perform(
                        delete("/api/users/logout")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginUser))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("사용자 로그아웃 (실패) 테스트")
    @Test
    void when_logout_expect_success_fail_exception() throws Exception {
        // given
        RequestSaveUser saveUser = RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");

        // when
        userService.addUser(saveUser);

        // then
        mockMvc.perform(
                        delete("/api/users/logout").session(session)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}