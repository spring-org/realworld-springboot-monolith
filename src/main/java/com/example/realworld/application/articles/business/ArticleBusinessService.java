package com.example.realworld.application.articles.business;

import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.ArticleDomainService;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserDomainService;
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

    // 조건에 따른 전체 글 리스트를 조회?
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiArticles searchPageArticles(RequestPageCondition condition) {

        List<Article> articles = articleRepository.searchPageArticle(condition);

        return ResponseMultiArticles.of(articles);
    }

    // 내가 follow한 사용자의 글을 페이징하여 조회( 커스텀 쿼리가 필요한 듯 )
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiArticles getFeedArticles(String email, Pageable pageable) {

        boolean exists = userDomainService.existsByEmail(email);
        if (!exists) {
            throw new NotFoundUserException("존재하지 않는 사용자입니다.");
        }
        // User(1) -> Follow(N) -> User(N) -> Article(N)
        List<Article> articles = articleRepository.searchPageFeed(email, pageable);

        return ResponseMultiArticles.of(articles);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseArticle getArticle(String slug) {

        Article findArticle = articleDomainService.getArticleOrElseThrow(slug);

        return ResponseArticle.from(findArticle);
    }

    @Transactional
    @Override
    public ResponseArticle postArticle(String email, RequestSaveArticle saveArticle) {

        User findUser = userDomainService.findUserByEmail(email);
        Article savedArticle = articleRepository.save(
                RequestSaveArticle.toEntity(saveArticle, findUser));

        findUser.addArticle(savedArticle);

        return ResponseArticle.from(savedArticle);
    }

    @Transactional
    @Override
    public ResponseArticle updateArticle(String email, String slug, RequestUpdateArticle updateArticle) {

        User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = findUser.getArticleBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
        findArticle.update(updateArticle.getTitle(), updateArticle.getDescription(), updateArticle.getBody());

        return ResponseArticle.from(findArticle);
    }

    @Transactional
    @Override
    public void deleteArticle(String email, String slug) {

        User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = findUser.getArticleBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));

        findUser.removeArticle(findArticle);
        articleRepository.delete(findArticle);
    }
}
