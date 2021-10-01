package com.example.realworld.application.tags.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TagTypeTest {

    @DisplayName("태그타입 전체 조회 테스트")
    @Test
    void when_all_expect_success_all_data() {
        // given
        Set<TagType> tagTypes = TagType.all();
        // when
        int size = tagTypes.size();
        // then
        assertThat(size).isEqualTo(9);
    }

    @DisplayName("특정 태그타입 조회 테스트")
    @Test
    void when_of_expect_success_find_TagType() {
        // given
        TagType expect = TagType.JAVA;
        // when
        String actual = TagType.from("Java");
        // then
        assertThat(actual).isEqualTo(expect.tagName());
    }

    @DisplayName("특정 태그타입 존재여부 테스트")
    @Test
    void when_exist_expect_success_exist_true() {
        // given
        TagType expect = TagType.CPP;
        // when
        String actual = TagType.from("C++");
        // then
        assertThat(actual).isEqualTo(expect.tagName());
    }
}