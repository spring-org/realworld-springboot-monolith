package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.example.realworld.application.users.dto.ResponseProfile;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseSingleComment {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String body;
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
