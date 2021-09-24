package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.users.dto.ResponseUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseSingleComment {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String body;
    private final ResponseUser author;

    private ResponseSingleComment(Comment comment, ResponseUser author) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.author = author;
    }

    public static ResponseSingleComment of(Comment comment) {
        return new ResponseSingleComment(comment, ResponseUser.of(comment.getAuthor()));
    }
}
