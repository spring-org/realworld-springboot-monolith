package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleBusinessService implements ArticleService {

    private final UserDomainService userDomainService;
    private final ArticleRepository articleRepository;
    private final ArticleDomainService articleDomainService;

    /**
     * 페이징 처리된 글 조회
     *
     * @param condition 페이징 및 조건 조회를 위한 파라미터
     * @return 페이징 및 조건 조회된 글 리스트를 반환
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiArticle searchPageArticles(RequestArticleCondition condition, Pageable pageable) {

        List<Article> articles = articleRepository.searchPageArticle(condition, pageable).toList();

        return ResponseMultiArticle.of(articles);
    }

    /**
     * 피드 데이터를 조회
     *
     * @param email    현재 사용자의 이메일 정보
     * @param pageable 페이징 및 정렬을 위한 파라미터
     * @return 피드 글 리스트 반환
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiArticle getFeedArticles(String email, Pageable pageable) {

        boolean exists = userDomainService.existsByEmail(email);
        if (!exists) {
            throw new NotFoundUserException();
        }
        // User(1) -> (N)Follow(1) -> Article(N)
        List<Article> articles = articleRepository.searchPageFeed(email, pageable);

        return ResponseMultiArticle.of(articles);
    }

    /**
     * 특정 글 조회
     *
     * @param slug 특정 글의 Slug
     * @return 특정 글을 반환
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseSingleArticle getArticle(String slug) {

        final Article findArticle = articleDomainService.getArticleOrElseThrow(slug);

        return ResponseSingleArticle.from(findArticle);
    }

    /**
     * 사용자의 새로운 글을 등록
     *
     * @param email       현재 사용자의 이메일 정보
     * @param saveArticle 새로운 글 등록을 위한 정보
     * @return 등록된 글을 반환
     */
    @Transactional
    @Override
    public ResponseSingleArticle postArticle(final String email, final RequestSaveArticle saveArticle) {

        final User findUser = userDomainService.findUserByEmail(email);
        Article savedArticle = articleDomainService.addArticle(saveArticle, findUser);
        findUser.addArticle(savedArticle);

        return ResponseSingleArticle.from(savedArticle);
    }

    /**
     * 사용자의 특정 글을 수정
     *
     * @param email         현재 사용자의 이메일 정보
     * @param slug          특정 글의 Slug
     * @param updateArticle 특정 글의 수정을 위한 정보
     * @return 수정된 글을 반환
     */
    @Transactional
    @Override
    public ResponseSingleArticle updateArticle(final String email, final String slug, final RequestUpdateArticle updateArticle) {

        final User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = findUser.getArticleBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);
        findArticle.update(updateArticle.getTitle(), updateArticle.getDescription(), updateArticle.getBody());

        return ResponseSingleArticle.from(findArticle);
    }

    /**
     * 사용자의 특정 글을 삭제
     *
     * @param email 현재 사용자의 이메일 정보
     * @param slug  특정 글의 Slug
     */
    @Transactional
    @Override
    public void deleteArticle(final String email, final String slug) {

        final User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = findUser.getArticleBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        findUser.removeArticle(findArticle);
        articleRepository.delete(findArticle);
    }
}
