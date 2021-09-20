package com.example.realworld.application.tags.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Table(name = "TB_TAG")
@Entity(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "TAG_ID", nullable = false)
    private Long id;

    private String tagName;

    private Tag(String tagName) {
        this.tagName = tagName;
    }

    public static Tag of(String tagName) {
        return new Tag(tagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }
}
