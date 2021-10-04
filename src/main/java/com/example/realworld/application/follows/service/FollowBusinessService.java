package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.exception.CannotSelfFollowException;
import com.example.realworld.application.follows.exception.DuplicatedFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowBusinessService implements FollowService {

    private final UserDomainService userDomainService;
    private final FollowDomainService followDomainService;

    /**
     * 현재 사용자와 특정 사용자 간의 관계를 형성
     *
     * @param toEmail          특정 사용자의 이메일 정보
     * @param currentUserEmail 현재 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자 간의 관계 정보 및 프로필 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile follow(final String currentUserEmail, final String toEmail) {

        if (currentUserEmail.equals(toEmail)) {
            throw new CannotSelfFollowException();
        }

        User fromUser = userDomainService.findUserByEmail(currentUserEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        if (toUser.isFollowing(fromUser)) {
            throw new DuplicatedFollowException();
        }

        Follow savedFollow = followDomainService.save(toUser, fromUser);
        toUser.follow(savedFollow);

        return ResponseProfile.ofProfile(fromUser, toUser);
    }

    /**
     * 현재 사용자와 특정 사용자 간의 연관 관계를 취소
     *
     * @param currentUserEmail 현재 사용자의 이메일 정보
     * @param toEmail          특정 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자의 언팔로우 정보를 포함한 프로파일 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile unFollow(final String currentUserEmail, final String toEmail) {

        if (currentUserEmail.equals(toEmail)) {
            throw new CannotSelfFollowException();
        }

        User fromUser = userDomainService.findUserByEmail(currentUserEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        if (!toUser.isFollowing(fromUser)) {
            throw new NotFoundFollowException();
        }

        Follow findFollow = toUser.findFollowing(fromUser);
        toUser.unFollow(findFollow);
        followDomainService.delete(findFollow);

        return ResponseProfile.ofProfile(fromUser, toUser);
    }
}
