package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.exception.DuplicateFollowException;
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
     * @param toEmail   특정 사용자의 이메일 정보
     * @param fromEmail 현재 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자 간의 관계 정보 및 프로필 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile follow(final String toEmail, final String fromEmail) {

        User toUser = userDomainService.findUserByEmail(toEmail);
        User fromUser = userDomainService.findUserByEmail(fromEmail);

        if (toUser.isFollowing(fromUser)) {
            throw new DuplicateFollowException("이미 팔로잉 중입니다.");
        }

        Follow savedFollow = followDomainService.save(toUser, fromUser);
        toUser.follow(savedFollow);

        return ResponseProfile.followProfile(toUser, fromUser);
    }

    /**
     * 현재 사용자와 특정 사용자 간의 연관 관계를 취소
     *
     * @param toEmail   특정 사용자의 이메일 정보
     * @param fromEmail 현재 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자의 언팔로우 정보를 포함한 프로파일 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile unFollow(final String toEmail, final String fromEmail) {
        User toUser = userDomainService.findUserByEmail(toEmail);
        User fromUser = userDomainService.findUserByEmail(fromEmail);

        if (!toUser.isFollowing(fromUser)) {
            throw new NotFoundFollowException("존재하지 않는 팔로우 관계입니다.");
        }

        Follow findFollow = toUser.findFollowing(fromUser);
        toUser.unFollow(findFollow);
        followDomainService.delete(findFollow);

        return ResponseProfile.followProfile(toUser, fromUser);
    }
}
