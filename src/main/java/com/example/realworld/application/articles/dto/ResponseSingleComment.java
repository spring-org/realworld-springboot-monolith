package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@JsonTypeName(value = "comment")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonPropertyOrder({"id", "createdAt", "updatedAt", "body", "author"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSingleComment {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String body;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private final ResponseProfile author;

    private ResponseSingleComment(Comment comment, ResponseProfile author) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.author = author;
    }

    public static ResponseSingleComment from(Comment comment) {
        return new ResponseSingleComment(comment, ResponseProfile.of(comment.getAuthor()));
    }
}
