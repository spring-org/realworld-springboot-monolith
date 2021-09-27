package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.Comment;
import com.example.realworld.application.users.persistence.User;
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

    public static Comment of(RequestSaveComment saveComment, User author, Article article) {
        return Comment.of(saveComment.getBody(), author, article);
    }
}
