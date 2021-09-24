package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.domain.Comment;
import com.example.realworld.application.articles.dto.RequestSaveComment;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.exception.NotFoundCommentException;
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
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));

        return article.getComments();
    }

    @Transactional
    @Override
    public Comment postComment(String email, String slug, RequestSaveComment saveComment) {

        User findUser = getUser(email);

        Article article = findUser.getArticle(slug);
        Comment savedComment = commentRepository.save(RequestSaveComment.of(saveComment, findUser));
        article.postComment(savedComment);

        return savedComment;
    }

    @Transactional
    @Override
    public void deleteComment(String email, String slug, Long id) {
        User findUser = getUser(email);
        Article findArticle = findUser.getArticle(slug);

        Comment findComment = findArticle.getComments().stream()
                .filter(comment -> comment.isMatches(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundCommentException("존재하지 않는 커멘트입니다."));

        boolean isAuth = findComment.isAuthor(findUser);
        if (isAuth) {
            commentRepository.delete(findComment);
            findArticle.deleteComment(findComment);
        } else {
            throw new UnauthorizedUserException("권한이 존재하지 않습니다.");
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }
}
