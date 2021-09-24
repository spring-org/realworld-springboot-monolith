package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.users.domain.User;
import lombok.Getter;

@Getter
public class RequestSaveComment {
    private final String body;

    private RequestSaveComment(String body) {
        this.body = body;
    }

    public static RequestSaveComment from(String body) {
        return new RequestSaveComment(body);
    }

    public static Comment of(RequestSaveComment saveComment, User author) {
        return Comment.of(saveComment.getBody(), author);
    }
}
