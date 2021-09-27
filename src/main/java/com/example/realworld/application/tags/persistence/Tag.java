package com.example.realworld.application.tags.persistence;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(name = "TB_TAG")
@Entity(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TAG_ID", nullable = false)
    private Long id;

    private String tagName;

    private Tag(String tagName) {
        this.tagName = tagName;
    }

    public static Tag of(String tagName) {
        return new Tag(tagName);
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName);
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }
}
