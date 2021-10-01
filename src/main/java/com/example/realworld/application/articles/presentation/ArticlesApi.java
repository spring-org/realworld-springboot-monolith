package com.example.realworld.application.articles.presentation;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.articles.service.CommentService;
import com.example.realworld.application.favorites.service.FavoriteArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesApi {

    public static final String EMAIL = "email";
    private final FavoriteArticleService favoriteArticleService;
    private final ArticleService articleService;
    private final CommentService commentService;

    /**
     * 전체 글 조회
     *
     * @param condition 페이징 및 조건 조회를 위한 파라미터
     * @return 페이징 및 조건 조회 글 리스트
     */
    @GetMapping
    public ResponseEntity<ResponseMultiArticle> getArticles(
            @Valid @RequestBody RequestPageCondition condition) {

        ResponseMultiArticle articles = articleService.searchPageArticles(condition);

        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    /**
     * Follow 한 사용자의 글(피드)들을 가져오는 인터페이스
     *
     * @param session  현재 사용자의 정보
     * @param pageable 피드 정보를 페이징 하기 위한 정보
     * @return 피드 리스트
     */
    @GetMapping(value = "/feed")
    public ResponseEntity<ResponseMultiArticle> feedArticle(
            HttpSession session, @PageableDefault(value = 20) Pageable pageable) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseMultiArticle feedArticles = articleService.getFeedArticles(email, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(feedArticles);
    }

    /**
     * 글 생성
     *
     * @param session     현재 사용자의 정보
     * @param saveArticle 글 등록을 위한 정보
     * @return 등록된 글 반환
     */
    @PostMapping
    public ResponseEntity<ResponseSingleArticle> createArticle(
            HttpSession session, @Valid @RequestBody RequestSaveArticle saveArticle) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleArticle savedArticle = articleService.postArticle(email, saveArticle);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    /**
     * 단일 글 상세 조회
     *
     * @param slug 특정 글의 slug
     * @return 특정 글을 반환
     */
    @GetMapping(value = "/{slug}")
    public ResponseEntity<ResponseSingleArticle> getArticle(
            @PathVariable("slug") String slug) {

        ResponseSingleArticle article = articleService.getArticle(slug);

        return ResponseEntity.status(HttpStatus.OK).body(article);
    }

    /**
     * 글 수정
     *
     * @param session       현재 사용자의 정보
     * @param slug          특정 글의 slug
     * @param updateArticle 특정 글을 수정하기 위한 정보
     * @return 수정된 글 반환
     */
    @PutMapping(value = "/{slug}")
    public ResponseEntity<ResponseSingleArticle> updateArticle(
            HttpSession session, @PathVariable("slug") String slug, @Valid @RequestBody RequestUpdateArticle updateArticle) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleArticle updatedArticle = articleService.updateArticle(email, slug, updateArticle);

        return ResponseEntity.status(HttpStatus.OK).body(updatedArticle);
    }

    /**
     * 글 삭제
     *
     * @param session 현재 사용자 정보
     * @param slug    특정 글의 slug
     * @return 204 코드 반환
     */
    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> deleteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        articleService.deleteArticle(email, slug);

        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 글에 커멘트 등록
     *
     * @param session     현재 사용자의 정보
     * @param slug        특정 글의 Slug
     * @param saveComment 커멘트 등록을 위한 정보
     * @return 등록된 커멘트의 정보
     */
    @PostMapping(value = "/{slug}/comments")
    public ResponseEntity<ResponseSingleComment> addCommentsToArticle(
            HttpSession session, @PathVariable("slug") String slug, @Valid @RequestBody RequestSaveComment saveComment) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleComment savedComment = commentService.postComment(email, slug, saveComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    /**
     * 특정 글의 모든 커멘트를 조회
     *
     * @param slug 특정 글의 Slug
     * @return 특정 글의 모든 커멘트를 반환
     */
    @GetMapping(value = "/{slug}/comments")
    public ResponseEntity<ResponseMultiComment> getCommentsFromAnArticle(
            @PathVariable("slug") String slug) {

        ResponseMultiComment comments = commentService.getCommentsByArticle(slug);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 커멘트 삭제
     *
     * @param session   현재 사용자의 정보
     * @param slug      특정 글의 Slug
     * @param commentId 특정 커멘트의 Id 정보
     * @return 204 코드 반환
     */
    @DeleteMapping(value = "/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComments(
            HttpSession session, @PathVariable("slug") String slug, @PathVariable("id") Long commentId) {

        String email = (String) session.getAttribute(EMAIL);
        commentService.deleteComment(email, slug, commentId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 관심 글로 등록
     *
     * @param session 현재 사용자의 정보
     * @param slug    특정 글의 Slug
     * @return 관심 글로 처리된 글의 정보를 반환
     */
    @PostMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseSingleArticle> favoriteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleArticle followArticle = favoriteArticleService.favoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(followArticle);
    }


    /**
     * 관심 글 취소
     *
     * @param session 현재 사용자의 정보
     * @param slug    특정 글의 Slug
     * @return 관심 글 취소 된 글의 정보를 반환
     */
    @DeleteMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseSingleArticle> unFavoriteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleArticle unfollowArticle = favoriteArticleService.unFavoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(unfollowArticle);
    }

}
