package com.example.realworld.application.articles.presentation;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.service.ArticleService;
import com.example.realworld.application.articles.service.CommentService;
import com.example.realworld.application.favorites.service.FavoriteArticleService;
import com.example.realworld.core.security.context.UserDetailsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesApi {

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
            @Valid @RequestBody RequestArticleCondition condition,
            @PageableDefault(value = 20) Pageable pageable) {

        ResponseMultiArticle articles = articleService.searchPageArticles(condition, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    /**
     * Follow 한 사용자의 글(피드)들을 가져오는 인터페이스
     *
     * @param userDetailsContext    현재 사용자의 정보
     * @param pageable              피드 정보를 페이징 하기 위한 정보
     * @return 피드 리스트
     */
    @GetMapping(value = "/feed")
    public ResponseEntity<ResponseMultiArticle> feedArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PageableDefault(value = 20) Pageable pageable) {

        String email = userDetailsContext.getUsername();
        ResponseMultiArticle feedArticles = articleService.getFeedArticles(email, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(feedArticles);
    }

    /**
     * 글 생성
     *
     * @param userDetailsContext    현재 사용자의 정보
     * @param saveArticle           글 등록을 위한 정보
     * @return 등록된 글 반환
     */
    @PostMapping
    public ResponseEntity<ResponseSingleArticle> createArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @Valid @RequestBody RequestSaveArticle saveArticle) {

        String email = userDetailsContext.getUsername();
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
     * @param userDetailsContext    현재 사용자의 정보
     * @param slug                  특정 글의 slug
     * @param updateArticle         특정 글을 수정하기 위한 정보
     * @return 수정된 글 반환
     */
    @PutMapping(value = "/{slug}")
    public ResponseEntity<ResponseSingleArticle> updateArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug,
            @Valid @RequestBody RequestUpdateArticle updateArticle) {

        String email = userDetailsContext.getUsername();
        ResponseSingleArticle updatedArticle = articleService.updateArticle(email, slug, updateArticle);

        return ResponseEntity.status(HttpStatus.OK).body(updatedArticle);
    }

    /**
     * 글 삭제
     *
     * @param userDetailsContext    현재 사용자의 정보
     * @param slug                  특정 글의 slug
     * @return 204 코드 반환
     */
    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> deleteArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug) {

        String email = userDetailsContext.getUsername();
        articleService.deleteArticle(email, slug);

        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 글에 커멘트 등록
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param slug               특정 글의 Slug
     * @param saveComment        커멘트 등록을 위한 정보
     * @return 등록된 커멘트의 정보
     */
    @PostMapping(value = "/{slug}/comments")
    public ResponseEntity<ResponseSingleComment> addCommentsToArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug,
            @Valid @RequestBody RequestSaveComment saveComment) {

        String email = userDetailsContext.getUsername();
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
     * @param userDetailsContext 현재 사용자의 정보
     * @param slug               특정 글의 Slug
     * @param commentId          특정 커멘트의 Id 정보
     * @return 204 코드 반환
     */
    @DeleteMapping(value = "/{slug}/comments/{commentId}")
    public ResponseEntity<Void> deleteComments(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug,
            @PathVariable("commentId") Long commentId) {

        String email = userDetailsContext.getUsername();
        commentService.deleteComment(email, slug, commentId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 관심 글로 등록
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param slug               특정 글의 Slug
     * @return 관심 글로 처리된 글의 정보를 반환
     */
    @PostMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseSingleArticle> favoriteArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug) {

        String email = userDetailsContext.getUsername();
        ResponseSingleArticle followArticle = favoriteArticleService.favoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(followArticle);
    }


    /**
     * 관심 글 취소
     *
     * @param userDetailsContext    현재 사용자의 정보
     * @param slug                  특정 글의 Slug
     * @return 관심 글 취소 된 글의 정보를 반환
     */
    @DeleteMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseSingleArticle> unFavoriteArticle(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("slug") String slug) {

        String email = userDetailsContext.getUsername();
        ResponseSingleArticle unfollowArticle = favoriteArticleService.unFavoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(unfollowArticle);
    }

}
