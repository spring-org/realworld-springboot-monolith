package com.example.realworld.application.tags.presentation;

import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.tags.persistence.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TagApiTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
    }

    @DisplayName("태그 전체 조회 테스트")
    @Test
    void when_getTags_expect_success_all_tags() throws Exception {
        tagRepository.saveAll(List.of(Tag.of("Java"), Tag.of("JavaScript"),Tag.of("R"),  Tag.of("Python")));

        mockMvc.perform(get("/api/tags"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}