package com.example.realworld.application.users.domain;

import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "TB_FOLLOW")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FOLLOW_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FROM_USER_ID")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "TO_USER_ID")
    private User toUser;

    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Follow following(User fromUser, User toUser) {
        return new Follow(fromUser, toUser);
    }

    public boolean isSameToUser(User toUser) {
        return this.toUser.isSameUser(toUser);
    }
}