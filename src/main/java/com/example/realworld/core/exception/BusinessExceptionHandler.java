package com.example.realworld.core.exception;

import com.example.realworld.application.articles.exception.DuplicatedFavoriteArticleException;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.exception.NotFoundCommentException;
import com.example.realworld.application.favorites.exception.NotFoundFavoriteArticleException;
import com.example.realworld.application.follows.exception.CannotSelfFollowException;
import com.example.realworld.application.follows.exception.DuplicatedFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.exception.UnauthorizedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseData unauthorizedUserException() {
        return ResponseData.from("접근 권한이 부족합니다.");
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseData duplicatedUserException() {
        return ResponseData.from("이미 존재하는 사용자입니다.");
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseData notFoundUserException() {
        return ResponseData.from("존재하지 않는 사용자입니다.");
    }

    @ExceptionHandler(DuplicatedFollowException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseData duplicatedFollowException() {
        return ResponseData.from("이미 팔로우 중입니다.");
    }

    @ExceptionHandler(CannotSelfFollowException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    protected ResponseData cannotSelfFollowException() {
        return ResponseData.from("본인을 팔로우 할 수 없습니다.");
    }

    @ExceptionHandler(NotFoundFollowException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseData notFoundFollowException() {
        return ResponseData.from("팔로우 정보가 존재하지 않습니다.");
    }

    @ExceptionHandler(NotFoundFavoriteArticleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseData notFoundFavArticleException() {
        return ResponseData.from("관심 글에 존재하지 않습니다.");
    }

    @ExceptionHandler(DuplicatedFavoriteArticleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseData duplicatedFavArticleException() {
        return ResponseData.from("이미 좋아요 누른 글입니다.");
    }

    @ExceptionHandler(NotFoundArticleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseData notFoundArticleException() {
        return ResponseData.from("존재하지 않는 글입니다.");
    }

    @ExceptionHandler(NotFoundCommentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseData notFoundCommentException() {
        return ResponseData.from("존재하지 않는 커멘트입니다.");
    }


}
