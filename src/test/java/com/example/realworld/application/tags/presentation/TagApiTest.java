package com.example.realworld.application.tags.presentation;

import com.example.realworld.application.BaseSpringBootTest;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.tags.persistence.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TagApiTest extends BaseSpringBootTest {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("전체 태그리스트 조회")
    @Test
    void when_getTags_expect_success_tagList() throws Exception {
        tagRepository.saveAll(List.of(Tag.of("Java"), Tag.of("R"), Tag.of("Python"), Tag.of("Node.js"), Tag.of("JavaScript"), Tag.of("Ruby")));

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagList.[0]").value("Java"))
                .andExpect(jsonPath("$.tagList.[1]").value("JavaScript"))
                .andExpect(jsonPath("$.tagList.[2]").value("Node.js"))
                .andExpect(jsonPath("$.tagList.[3]").value("Python"))
                .andExpect(jsonPath("$.tagList.[4]").value("R"))
                .andExpect(jsonPath("$.tagList.[5]").value("Ruby"))
        ;
    }
}