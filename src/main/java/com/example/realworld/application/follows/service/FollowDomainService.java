package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.follows.persistence.repository.FollowRepository;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class FollowDomainService {

    private final FollowRepository followRepository;

    /**
     * 현재 사용자와 특정 사용자 간의 관계를 형성
     *
     * @param fromUser 현재 사용자
     * @param toUser   특정 사용자
     * @return 현재 사용자와 특정 사용자 간의 관계를 형성
     */
    @Transactional
    public Follow save(final User fromUser, final User toUser) {

        final Follow following = Follow.following(fromUser, toUser);

        return followRepository.save(following);
    }
}