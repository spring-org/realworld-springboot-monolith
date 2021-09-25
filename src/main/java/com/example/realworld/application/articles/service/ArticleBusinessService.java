package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.follows.domain.Follow;
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

    // 조건에 따른 전체 글 리스트를 조회?
    @Override
    public ResponseMultiArticles searchPageArticles(RequestPageCondition condition) {

        List<Article> articles = articleRepository.searchPageArticle(condition);

        return ResponseMultiArticles.of(articles);
    }

    // 내가 follow한 사용자의 글을 페이징하여 조회( 커스텀 쿼리가 필요한 듯 )
    @Override
    public ResponseMultiArticles getFeedArticles(String email, Pageable pageable) {

        User findUser = getUser(email);
        Set<Follow> following = findUser.getFollowing();

        List<Article> collect = following.stream()
                .map(Follow::getToUser)
                .flatMap(user -> user.getArticles().stream())
                .collect(Collectors.toList());

        return ResponseMultiArticles.of(collect);
    }

    @Override
    public ResponseArticle getArticle(String slug) {

        Article findArticle = getOrElseThrow(slug);

        return ResponseArticle.from(findArticle);
    }

    @Transactional
    @Override
    public ResponseArticle postArticle(String email, RequestSaveArticle saveArticle) {

        User findUser = getUser(email);
        Article savedArticle = articleRepository.save(
                RequestSaveArticle.toEntity(saveArticle, findUser));

        findUser.postArticles(savedArticle);

        return ResponseArticle.from(savedArticle);
    }

    @Transactional
    @Override
    public ResponseArticle updateArticle(String email, String slug, RequestUpdateArticle updateArticle) {

        User findUser = getUser(email);
        Article findArticle = findUser.getArticle(slug);
        findArticle.update(updateArticle.getTitle(), updateArticle.getDescription(), updateArticle.getBody());

        return ResponseArticle.from(findArticle);
    }

    @Transactional
    @Override
    public void deleteArticle(String email, String slug) {

        User findUser = getUser(email);
        Article findArticle = findUser.getArticle(slug);

        findUser.removeArticle(findArticle);
        articleRepository.delete(findArticle);
    }

    private Article getOrElseThrow(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }
}
