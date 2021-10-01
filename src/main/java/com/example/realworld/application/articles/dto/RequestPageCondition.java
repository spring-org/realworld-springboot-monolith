package com.example.realworld.application.articles.dto;

import com.example.realworld.application.tags.persistence.TagType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPageCondition {

    private TagType tag;
    private String author;
    private String favorited;
    private Integer limit = 20;
    private Integer offset = 0;

    private RequestPageCondition(TagType tag, String author, String favorited, Integer limit, Integer offset) {
        this.tag = tag;
        this.author = author;
        this.favorited = favorited;
        this.limit = limit;
        this.offset = offset;
    }

    public static RequestPageCondition of(String tag, String author, String favorited, Integer limit, Integer offset) {
        return new RequestPageCondition(TagType.of(tag), author, favorited, limit, offset);
    }
}
