package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.TagType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@JsonTypeName(value = "tag")
@Getter
public class ResponseSingleTag {

    private final String tagName;

    private ResponseSingleTag(String tagName) {
        this.tagName = tagName;
    }

    public static ResponseSingleTag from(TagType tag) {
        return new ResponseSingleTag(tag.tagName());
    }
}
