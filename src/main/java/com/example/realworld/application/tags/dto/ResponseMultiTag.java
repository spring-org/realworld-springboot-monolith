package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.TagType;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ResponseMultiTag {

    @JsonUnwrapped
    private final Set<String> tagList;

    private ResponseMultiTag(Set<String> tagList) {
        this.tagList = tagList;
    }

    public static ResponseMultiTag from(Set<TagType> tags) {
        Set<String> responseSingleTags = tags.stream()
                .map(tagType -> String.valueOf(tagType.tagName()))
                .collect(Collectors.toSet());
        return new ResponseMultiTag(responseSingleTags);
    }
}
