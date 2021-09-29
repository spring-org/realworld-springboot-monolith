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
     * @param fromEmail 현재 사용자의 이메일 정보
     * @param toEmail   특정 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자 간의 관계 정보 및 프로필 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile follow(final String fromEmail, final String toEmail) {

        User fromUser = userDomainService.findUserByEmail(fromEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        if (fromUser.isFollowing(toUser)) {
            throw new DuplicateFollowException("이미 팔로잉 중입니다.");
        }

        Follow savedFollow = followDomainService.save(fromUser, toUser);
        fromUser.follow(savedFollow);

        return ResponseProfile.of(fromUser, toUser);
    }

    /**
     * 현재 사용자와 특정 사용자 간의 연관 관계를 취소
     *
     * @param fromEmail 현재 사용자의 이메일 정보
     * @param toEmail   특정 사용자의 이메일 정보
     * @return 현재 사용자와 특정 사용자의 언팔로우 정보를 포함한 프로파일 정보를 반환
     */
    @Transactional
    @Override
    public ResponseProfile unFollow(final String fromEmail, final String toEmail) {
        User fromUser = userDomainService.findUserByEmail(fromEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        if (!fromUser.isFollowing(toUser)) {
            throw new NotFoundFollowException("존재하지 않는 팔로우 관계입니다.");
        }

        Follow findFollow = fromUser.findFollowing(toUser);
        fromUser.unFollow(findFollow);
        followDomainService.delete(findFollow);

        return ResponseProfile.of(fromUser, toUser);
    }
}
