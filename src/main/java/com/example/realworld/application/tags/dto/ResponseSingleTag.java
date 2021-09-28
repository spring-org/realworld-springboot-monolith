package com.example.realworld.application.tags.dto;

import com.example.realworld.application.tags.persistence.Tag;
import lombok.Getter;

@Getter
public class ResponseSingleTag {

    private ResponseSingleTag(Tag tag) {
    }

    public static ResponseSingleTag from(Tag tag) {
        return new ResponseSingleTag(tag);
    }
}
