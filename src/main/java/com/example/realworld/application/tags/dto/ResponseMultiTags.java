package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.Tag;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ResponseMultiTags {

    private final Set<ResponseSingleTag> tags;

    private ResponseMultiTags(Set<ResponseSingleTag> tags) {
        this.tags = tags;
    }

    public static ResponseMultiTags from(Set<Tag> tags) {
        Set<ResponseSingleTag> responseSingleTags = tags.stream()
                .map(ResponseSingleTag::from)
                .collect(Collectors.toSet());
        return new ResponseMultiTags(responseSingleTags);
    }
}
