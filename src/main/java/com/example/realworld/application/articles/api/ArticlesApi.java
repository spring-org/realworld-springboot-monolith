package com.example.realworld.application.articles.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticlesApi {

    /**
     * 글 리스트, 인증 옵션
     *
     * @return List Articles
     */
    @GetMapping
    public ResponseEntity<?> getArticles() {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글 피드, 인증 필요
     *
     * @return List Articles
     */
    @GetMapping(value = "/feed")
    public ResponseEntity<?> feedArticle() {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 단일 글 조회, 인증 불필요
     *
     * @param slug
     * @return
     */
    @GetMapping(value = "/{slug}")
    public ResponseEntity<?> getArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글 생성, 인증 필요
     *
     * @return Article
     */
    @PostMapping
    public ResponseEntity<?> createArticle() {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글 수정, 인증 필요
     *
     * @param slug
     * @return Article
     */
    @PutMapping(value = "/{slug}")
    public ResponseEntity<?> updateArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글 지우기, 인증 필요
     *
     * @param slug
     */
    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.noContent().build();
    }

    /**
     * 글에 커멘트 달기, 인증 필요
     *
     * @return
     */
    @PostMapping(value = "/{slug}/comments")
    public ResponseEntity<?> addCommentsToArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글에 달린 커멘트 가져오기, 인증 옵션
     *
     * @return multiple comments
     */
    @GetMapping(value = "/{slug}/comments")
    public ResponseEntity<?> getCommentsFromAnArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 커멘트 삭제, 인증 필요
     *
     * @param slug
     * @param id
     */
    @DeleteMapping(value = "/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComments(
            @PathVariable("slug") String slug, @PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }

    /**
     * 글 좋아요 요청, 인증 필요
     *
     * @param slug
     * @return Article
     */
    @PostMapping(value = "/{slug}/favorite")
    public ResponseEntity<?> favoriteArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * 글 좋아요 취소, 인증 필요
     *
     * @param slug
     * @return Article
     */
    @DeleteMapping(value = "/{slug}/favorite")
    public ResponseEntity<?> unFavoriteArticle(@PathVariable("slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
