package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.Tag;
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

    public static ResponseMultiTag from(Set<Tag> tags) {
        Set<String> responseSingleTags = tags.stream()
                .map(tag -> String.valueOf(tag.name()))
                .collect(Collectors.toUnmodifiableSet());
        return new ResponseMultiTag(responseSingleTags);
    }
}
