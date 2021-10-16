package com.example.realworld.application;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class IntegrationUtilTest {

    static ResultMatcher matchesCurrentUserInfo() {
        return matchAll(
                jsonPath("email").value("seokrae@gmail.com"),
                jsonPath("userName").value("seokrae")
        );
    }

    static ResultMatcher matchesOtherUserInfo() {
        return matchAll(
                jsonPath("email").value("seok@gmail.com"),
                jsonPath("userName").value("seok")
        );
    }

}
