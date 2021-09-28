package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.dto.ResponseMultiComments;
import com.example.realworld.application.articles.dto.ResponseSingleComment;

public interface CommentService {
    // 특정 글의 커멘트 리스트 조회
    ResponseMultiComments getCommentsByArticle(String slug);

    // 특정 글에 커멘트 등록
    ResponseSingleComment postComment(String email, String slug, RequestSaveComment saveComment);

    // 특정 글의 커멘트 삭제
    void deleteComment(String email, String slug, Long id);
}
