package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.articles.dto.RequestSaveComment;

import java.util.Set;

public interface CommentService {
    Comment postComment(String email, String slug, RequestSaveComment saveComment);

    Set<Comment> getCommentsByArticle(String slug);

    void deleteComment(String email, String slug, Long id);
}
