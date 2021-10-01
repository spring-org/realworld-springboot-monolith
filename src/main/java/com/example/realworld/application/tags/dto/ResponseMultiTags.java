package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.TagType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseMultiTags {

    private final List<ResponseSingleTag> tags;

    private ResponseMultiTags(List<ResponseSingleTag> tags) {
        this.tags = tags;
    }

    public static ResponseMultiTags from(List<TagType> tags) {
        List<ResponseSingleTag> responseSingleTags = tags.stream()
                .map(ResponseSingleTag::from)
                .collect(Collectors.toList());
        return new ResponseMultiTags(responseSingleTags);
    }
}
