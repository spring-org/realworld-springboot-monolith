package com.example.realworld.application.articles.business;

import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.dto.ResponseMultiComments;
import com.example.realworld.application.articles.dto.ResponseSingleComment;

public interface CommentService {
    ResponseSingleComment postComment(String email, String slug, RequestSaveComment saveComment);

    ResponseMultiComments getCommentsByArticle(String slug);

    void deleteComment(String email, String slug, Long id);
}
