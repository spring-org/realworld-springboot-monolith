package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity(name = "comments")
@Table(name = "TB_COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_ID", nullable = false)
    private Long id;

    private String body;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    private Article article;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Comment(String body, User author, Article article) {
        this.body = body;
        this.author = author;
        this.article = article;
    }

    public static Comment of(String body, User author, Article article) {
        return new Comment(body, author, article);
    }

    public void update(String body) {
        this.body = body;
    }

    public boolean isMatches(Long id) {
        return this.id.equals(id);
    }

    public boolean isAuthor(User currentUser) {
        return this.author.equals(currentUser);
    }

    // jacoco 라이브러리가 lombok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
