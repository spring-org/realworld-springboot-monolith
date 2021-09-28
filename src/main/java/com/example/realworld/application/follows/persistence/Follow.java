package com.example.realworld.application.follows.persistence;

import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.persistence.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "follows")
@Table(name = "TB_FOLLOW")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FOLLOW_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_USER_ID")
    private User fromUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_USER_ID")
    private User toUser;

    private Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Follow following(User fromUser, User toUser) {
        return new Follow(fromUser, toUser);
    }

    public boolean isSameToUser(User toUser) {
        return this.toUser.isSameUser(toUser);
    }

    public User fromUser() {
        return fromUser;
    }

    public User toUser() {
        return toUser;
    }
}