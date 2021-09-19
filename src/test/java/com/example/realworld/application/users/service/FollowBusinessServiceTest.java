package com.example.realworld.application.users.service;

import com.example.realworld.application.users.domain.Follow;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.FollowRepository;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowBusinessServiceTest {

    private FollowBusinessService followBusinessService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        followBusinessService = new FollowBusinessService(userRepository, followRepository);
    }

    @DisplayName("사용자 간의 팔로우 테스트")
    @Test
    void when_follow_expect_success_user_relationship(){
        User fromUser = User.of("seok1@gmail.com", "1234");
        User toUser = User.of("seok2@gmail.com", "1234");
        Follow newFollow = Follow.following(fromUser, toUser);

        given(userRepository.findByEmail(any())).willReturn(of(fromUser));
        given(userRepository.findByEmail(any())).willReturn(of(toUser));
        given(followRepository.save(any())).willReturn(newFollow);

        followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());

        User user = userRepository.findByEmail(fromUser.getEmail())
                .orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));

        boolean following = user.isFollowing(toUser);

        assertThat(following).isTrue();
    }

    @DisplayName("사용자 간의 언팔로우 테스트")
    @Test
    void when_unFollow_expect_success_single(){
        User fromUser = User.of("seok1@gmail.com", "1234");
        User toUser = User.of("seok2@gmail.com", "1234");
        Follow newFollow = Follow.following(fromUser, toUser);

        given(userRepository.findByEmail(any())).willReturn(of(fromUser));
        given(userRepository.findByEmail(any())).willReturn(of(toUser));
        given(followRepository.save(any())).willReturn(newFollow);

        followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());

        followBusinessService.unFollow(fromUser.getEmail(), toUser.getEmail());

        then(followRepository).should(times(1)).delete(newFollow);
//
//        User user = userRepository.findByEmail(fromUser.getEmail())
//                .orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));
//
//        boolean following = user.isFollowing(toUser);
//
//        assertThat(following).isFalse();
    }
}