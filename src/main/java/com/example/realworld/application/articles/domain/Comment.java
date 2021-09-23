package com.example.realworld.application.articles.domain;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@ToString
@Table(name = "TB_COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    private Comment(String body, User author) {
        this.body = body;
        this.author = author;
    }

    public static Comment of(String body, User author) {
        return new Comment(body, author);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
