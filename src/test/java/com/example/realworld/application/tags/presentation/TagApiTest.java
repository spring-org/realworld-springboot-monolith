package com.example.realworld.application.tags.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.tags.persistence.repository.TagRepository;
import com.example.realworld.application.tags.service.TagService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TagApiTest extends BaseSpringBootTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
    }

    @DisplayName("태그 전체 조회")
    @Test
    void testCase1() throws Exception {
        tagRepository.saveAll(List.of(Tag.of("Java"), Tag.of("JavaScript"), Tag.of("Python")));

        mockMvc.perform(get("/api/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("tagList[0]").value("Java"))
                .andExpect(jsonPath("tagList[1]").value("JavaScript"))
                .andExpect(jsonPath("tagList[2]").value("Python"));
    }
}