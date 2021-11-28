package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.follows.persistence.FollowFactory;
import com.example.realworld.application.follows.persistence.repository.FollowRepository;
import com.example.realworld.application.users.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class FollowDomainServiceTest {

    @InjectMocks
    private FollowDomainService followDomainService;

    @Mock
    private FollowRepository followRepository;

    @DisplayName("팔로우 테스트")
    @Test
    void followTest() {
        User fromUser = createUser("fromUser@gmail.com");
        User toUser = createUser("toUser@gmail.com");

        Follow following = FollowFactory.following(fromUser, toUser);
        given(followRepository.save(any())).willReturn(following);

        followDomainService.save(fromUser, toUser);

    }
}