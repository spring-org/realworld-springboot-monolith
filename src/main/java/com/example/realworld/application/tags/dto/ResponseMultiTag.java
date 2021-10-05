package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.Tag;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ResponseMultiTag {

    private final List<String> tagList;

    private ResponseMultiTag(List<String> tagList) {
        this.tagList = tagList;
    }

    public static ResponseMultiTag from(Set<Tag> tags) {
        List<String> responseSingleTags = tags.stream()
                .sorted(Comparator.comparing(Tag::name))
                .map(tag -> String.valueOf(tag.name()))
                .collect(Collectors.toUnmodifiableList());
        return new ResponseMultiTag(responseSingleTags);
    }
}
