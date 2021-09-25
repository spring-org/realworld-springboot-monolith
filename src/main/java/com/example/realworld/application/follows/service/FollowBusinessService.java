package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.domain.Follow;
import com.example.realworld.application.follows.exception.DuplicateFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.repository.FollowRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowBusinessService implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    @Override
    public ResponseProfile followUser(String fromEmail, String toEmail) {

        User fromUser = findUser(fromEmail);
        User toUser = findUser(toEmail);

        if (fromUser.isFollowing(toUser)) {
            throw new DuplicateFollowException("이미 팔로잉 중입니다.");
        } else {
            Follow newFollow = Follow.following(fromUser, toUser);
            Follow savedFollow = followRepository.save(newFollow);
            fromUser.follow(savedFollow);
        }
        return ResponseProfile.of(fromUser, toUser);
    }

    @Transactional
    @Override
    public ResponseProfile unFollow(String fromEmail, String toEmail) {
        User fromUser = findUser(fromEmail);
        User toUser = findUser(toEmail);

        if (fromUser.isFollowing(toUser)) {
            Follow findFollow = fromUser.findFollowing(toUser);
            fromUser.unFollow(findFollow);
            followRepository.delete(findFollow);
        } else {
            throw new NotFoundFollowException("존재하지 않는 팔로우 관계입니다.");
        }
        return ResponseProfile.of(fromUser, toUser);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }

}
