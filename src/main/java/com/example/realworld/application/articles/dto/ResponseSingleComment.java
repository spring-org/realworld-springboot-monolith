package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id", "body", "author", "createdAt", "updatedAt"})
@JsonRootName(value = "comment")
public class ResponseSingleComment {

    private final Long id;
    private final String body;
    private final ResponseProfile author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

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
