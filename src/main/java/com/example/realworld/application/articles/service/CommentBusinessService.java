package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.articles.repository.CommentRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import com.example.realworld.core.exception.UnauthorizedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentBusinessService implements CommentService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public Set<Comment> getCommentsByArticle(String slug) {

        Article findArticle = getArticle(slug);

        return findArticle.getComments();
    }

    @Transactional
    @Override
    public Comment postComment(final String email, final String slug, final RequestSaveComment saveComment) {

        User findUser = getUser(email);
        Article article = getArticle(slug);

        Comment savedComment = commentRepository.save(RequestSaveComment.of(saveComment, findUser, article));
        article.addComment(savedComment);

        return savedComment;
    }

    @Transactional
    @Override
    public void deleteComment(final String email, final String slug, final Long commentId) {
        User findUser = getUser(email);
        Article findArticle = getArticle(slug);

        Comment findComment = findArticle.getComments(commentId);

        boolean isAuth = findComment.isAuthor(findUser);
        if (isAuth) {
            commentRepository.delete(findComment);
            findArticle.removeComment(findComment);
        } else {
            throw new UnauthorizedUserException("권한이 존재하지 않습니다.");
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }

    private Article getArticle(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }
}
