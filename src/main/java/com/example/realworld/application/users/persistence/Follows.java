package com.example.realworld.application.users.persistence;

import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.persistence.Follow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follows implements Serializable {

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    @ToString.Exclude
    private final Set<Follow> followers = new HashSet<>();

    public static Follows init() {
        return new Follows();
    }

    public void addFollowing(Follow follow) {
        following.add(follow);
    }

    public void removeFollowing(Follow follow) {
        following.remove(follow);
    }

    public void addFollowers(Follow follow) {
        followers.add(follow);
    }

    public void removeFollowers(Follow follow) {
        followers.remove(follow);
    }

    public boolean isFollowing(User toUser) {
        return following.stream()
                .anyMatch(follow -> follow.isSameToUser(toUser));
    }

    public boolean isFollowers(User toUser) {
        return followers.stream()
                .anyMatch(follow -> follow.isSameToUser(toUser));
    }

    public Follow findFollowing(User toUser) {
        return following.stream()
                .filter(follow -> follow.isSameToUser(toUser))
                .findFirst()
                .orElseThrow(NotFoundFollowException::new);
    }

    public int followingSize() {
        return following.size();
    }
}
