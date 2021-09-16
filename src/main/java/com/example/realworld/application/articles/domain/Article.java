package com.example.realworld.application.articles.domain;

import com.example.realworld.application.articles.exception.NotFoundCommentException;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@ToString
@Table(name = "TB_ARTICLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ARTICLE_ID", nullable = false)
    private Long id;

    private String slug;

    private String title;

    private String description;

    private String body;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean favorited;

    private Integer favoritesCount;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User author;

    @OneToMany
    @JoinColumn(name = "COMMENT_ID")
    private final Set<Comment> comments = new HashSet<>();

    @Builder
    public Article(
            String title, String description, String body,
            boolean favorited, Integer favoritesCount, User author) {
        this.slug = makeSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = author;
    }

    private String makeSlug(String title) {
        return String.format("%s-%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), title);
    }

    // Article
    public String author() {
        return author.getProfile().getUserName();
    }

    public static Article of(
            String title, String description, String body, User author) {
        return new Article(title, description, body, false, 0, author);
    }

    public void update(String title, String description, String body) {
        if (StringUtils.hasText(title)) {
            this.title = title;
            this.slug = makeSlug(title);
        }
        if (StringUtils.hasText(description)) {
            this.description = description;
        }
        if (StringUtils.hasText(body)) {
            this.body = body;
        }
    }

    // Comment
    public void postComment(Comment comment) {
        this.comments.add(comment);
    }

    public Comment findComment(String body) {
        return this.comments.stream()
                .filter(comment -> comment.getBody().equals(body))
                .findAny()
                .orElseThrow(() -> new NotFoundCommentException("존재하지 않는 커멘트입니다."));
    }

    public boolean isMatches(String title) {
        return this.title.equals(title);
    }
}
