package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.persistence.Tag;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.example.realworld.application.articles.persistence.QArticle.article;
import static com.example.realworld.application.follows.persistence.QFollow.follow;
import static com.example.realworld.application.users.persistence.QUser.user;

public class ArticleRepositoryImpl implements ArticleQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Article> searchPageArticle(RequestPageCondition condition) { // string
        BooleanBuilder builder = new BooleanBuilder();
        // TODO 확인 필요..
        if (StringUtils.hasText(condition.getTag())) {
            builder.and(article.tags.contains(Tag.of(condition.getTag())));
        }
        if (StringUtils.hasText(condition.getAuthor())) {
            //
            builder.and(article.author.email.eq(condition.getAuthor()));
        }
        if (StringUtils.hasText(condition.getFavorited())) {
            builder.and(article.author.profile.userName.eq(condition.getFavorited()));
        }
        return Collections.unmodifiableList(queryFactory
                .selectFrom(article)
                .where(builder)
                .offset(condition.getOffset())
                .limit(condition.getLimit())
                .fetch());
    }

    @Override
    public List<Article> searchPageFeed(String email, Pageable pageable) {
        return Collections.unmodifiableList(queryFactory
                .select(article)
                .from(user)
                .innerJoin(follow)
                .on(follow.fromUser.id.eq(user.id))
                .innerJoin(follow)
                .on(follow.toUser.id.eq(article.author.id))
                .innerJoin(article)
                .on(article.author.id.eq(follow.toUser.id))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch());
    }
}
