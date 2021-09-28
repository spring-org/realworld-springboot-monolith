package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.dto.ResponseMultiComments;
import com.example.realworld.application.articles.dto.ResponseSingleComment;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.Comment;
import com.example.realworld.application.articles.persistence.repository.CommentRepository;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.service.UserDomainService;
import com.example.realworld.core.exception.UnauthorizedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentBusinessService implements CommentService {

    private final ArticleDomainService articleDomainService;
    private final UserDomainService userDomainService;
    private final CommentRepository commentRepository;

    /**
     * 특정 글의 전체 커멘트를 조회
     *
     * @param slug 특정 글의 Slug
     * @return 특정 글의 전체 커멘트를 반환
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiComments getCommentsByArticle(String slug) {

        Article findArticle = articleDomainService.getArticleOrElseThrow(slug);

        return ResponseMultiComments.from(findArticle.getComments());
    }

    /**
     * 특정 글에 커멘트 등록
     *
     * @param email       현재 사용자의 이메일 정보
     * @param slug        특정 글의 Slug
     * @param saveComment 새로운 커멘트 등록을 위한 정보
     * @return 등록된 커멘트를 반환
     */
    @Transactional
    @Override
    public ResponseSingleComment postComment(
            final String email, final String slug, final RequestSaveComment saveComment) {

        User findUser = userDomainService.findUserByEmail(email);
        Article article = articleDomainService.getArticleOrElseThrow(slug);

        Comment savedComment = commentRepository.save(RequestSaveComment.of(saveComment, findUser, article));
        article.addComment(savedComment);

        return ResponseSingleComment.from(savedComment);
    }

    /**
     * 특정 글의 커멘트 삭제
     *
     * @param email     현재 사용자의 이메일 정보
     * @param slug      특정 글의 slug
     * @param commentId 특정 커멘트 구분 Id
     */
    @Transactional
    @Override
    public void deleteComment(final String email, final String slug, final Long commentId) {
        User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = articleDomainService.getArticleOrElseThrow(slug);

        Comment findComment = findArticle.getComments(commentId);

        boolean isAuth = findComment.isAuthor(findUser);
        if (isAuth) {
            commentRepository.delete(findComment);
            findArticle.removeComment(findComment);
        } else {
            throw new UnauthorizedUserException("권한이 없습니다.");
        }
    }
}
