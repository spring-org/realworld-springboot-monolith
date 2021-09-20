package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.repository.ArticleQuerydslRepository;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.users.domain.Follow;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleBusinessService implements ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleQuerydslRepository articleQueryDslRepository;

    // 조건에 따른 전체 글 리스트를 조회?
    @Override
    public List<Article> getArticles(RequestPageCondition condition) {
        return articleQueryDslRepository.searchPageArticle(condition);
    }

    // 내가 follow한 사용자의 글을 페이징하여 조회( 커스텀 쿼리가 필요한 듯 )
    @Override
    public List<Article> getFeedArticles(String email, Pageable pageable) {

        User findUser = getUser(email);

        Set<Follow> following = findUser.getFollowing();

        return following.stream()
                .map(Follow::getToUser)
                .flatMap(user -> user.getArticles().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Article getArticle(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }

    @Transactional
    @Override
    public Article postArticle(String email, RequestSaveArticle saveArticle) {

        User findUser = getUser(email);
        Article article = RequestSaveArticle.toEntity(saveArticle, findUser);

        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article updateArticle(String email, String slug, RequestUpdateArticle updateArticle) {

        User findUser = getUser(email);
        Article findArticle = findUser.getArticle(slug);
        findArticle.update(updateArticle.getTitle(), updateArticle.getDescription(), updateArticle.getBody());

        return findArticle;
    }

    @Transactional
    @Override
    public void deleteArticle(String email, String slug) {

        User findUser = getUser(email);
        Article findArticle = findUser.getArticle(slug);

        articleRepository.delete(findArticle);
    }

    @Transactional
    @Override
    public Article favoriteArticle(String email, String slug) {
        User findUser = getUser(email);
        Article article = getArticle(slug);

        findUser.favoriteArticle(article);
        return null;
    }

    @Transactional
    @Override
    public Article unFavoriteArticle(String email, String slug) {
        User findUser = getUser(email);
        Article article = getArticle(slug);

        findUser.unFavoriteArticle(article);

        return null;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }
}
