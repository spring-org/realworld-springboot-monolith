package com.example.realworld.application.tags.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TagApiTest {

    @Autowired
    public MockMvc mockMvc;

    @DisplayName("태그 조회")
    @Test
    void testCase1() throws Exception {
        mockMvc.perform(get("/api/tags"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}