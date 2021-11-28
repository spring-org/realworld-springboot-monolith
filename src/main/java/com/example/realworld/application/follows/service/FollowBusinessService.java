package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.exception.DuplicatedFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.FollowUserRelationShip;
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
    public ResponseProfile follow(String currentUserEmail, String toEmail) {

        User fromUser = userDomainService.findUserByEmail(currentUserEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);

        if (relationShip.isFollowing(toUser)) {
            throw new DuplicatedFollowException();
        }

        Follow follower = followDomainService.save(fromUser, toUser);
        Follow followee = followDomainService.save(toUser, fromUser);

        relationShip.following(follower);
        relationShip.followers(followee);

        return ResponseProfile.ofProfile(relationShip.user(), toUser);
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
    public ResponseProfile unFollow(String currentUserEmail, String toEmail) {

        User fromUser = userDomainService.findUserByEmail(currentUserEmail);
        User toUser = userDomainService.findUserByEmail(toEmail);

        FollowUserRelationShip fromUserRelationShip = new FollowUserRelationShip(fromUser);
        if (!fromUserRelationShip.isFollowing(toUser)) {
            throw new NotFoundFollowException();
        }

        Follow findFollowing = fromUserRelationShip.findFollowing(toUser);
        fromUserRelationShip.unFollowing(findFollowing);

        FollowUserRelationShip toUserRelationShip = new FollowUserRelationShip(toUser);
        Follow findFollower = toUserRelationShip.findFollowing(fromUser);
        toUserRelationShip.unFollowers(findFollower);

        followDomainService.delete(findFollowing);
        followDomainService.delete(findFollower);

        return ResponseProfile.ofProfile(fromUser, toUser);
    }
}
