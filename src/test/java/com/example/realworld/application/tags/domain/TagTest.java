package com.example.realworld.application.tags.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @DisplayName("태그 생성 및 비교 테스트")
    @Test
    void when_generateTag_expected_success_newTag() {

        Tag tag = Tag.of("Java");

        assertThat(tag.getTagName()).isEqualTo("Java");
    }
}