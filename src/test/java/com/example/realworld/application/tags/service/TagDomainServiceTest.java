package com.example.realworld.application.tags.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TagDomainServiceTest {

    @Autowired
    private TagDomainService tagDomainService;

    @DisplayName("전체 태그 데이터 조회")
    @Test
    void when_findAll_expect_success_tags() {

    }
}