package com.example.realworld.application.tags.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @DisplayName("태그 객체 비교 테스트")
    @Test
    void when_equals_when_equal_tag() {
        // given
        Tag actual = Tag.of("Java");
        // when
        Tag expected = Tag.of("Java");
        // then
        assertThat(actual).isEqualTo(expected);
    }
}
