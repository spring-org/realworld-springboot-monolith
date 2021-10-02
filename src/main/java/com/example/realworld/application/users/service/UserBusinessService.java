package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.persistence.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBusinessService implements UserService {

    private final UserDomainService userDomainService;

    /**
     * 새로운 사용자 등록
     *
     * @param saveUser 새로운 사용자 등록을 위한 정보
     * @return 등록된 사용자 정보 반환
     */
    @Transactional
    @Override
    public ResponseUser postUser(final RequestSaveUser saveUser) {
        boolean existsUser = userDomainService.existsByEmail(saveUser.getEmail());

        if (existsUser) {
            throw new DuplicateUserException();
        }

        User savedUser = userDomainService.save(RequestSaveUser.toEntity(saveUser));
        return ResponseUser.of(savedUser);
    }

    /**
     * 사용자 정보 수정
     *
     * @param email      현재 사용자의 이메일 정보
     * @param updateUser 사용자 정보 수정을 위한 정보
     * @return 수정된 사용자 정보 반환
     */
    @Transactional
    @Override
    public ResponseUser updateUser(String email, RequestUpdateUser updateUser) {

        User user = userDomainService.findUserByEmail(email);
        user.update(updateUser);

        return ResponseUser.of(user);
    }

    /**
     * 사용자 정보 조회
     *
     * @param email 현재 사용자의 이메일 정보
     * @return 현재 사용자의 정보 반환
     */
    @Override
    public ResponseUser getUserByEmail(String email) {

        User findUser = userDomainService.findUserByEmail(email);

        return ResponseUser.of(findUser);
    }

    /**
     * 현재 사용자의 프로필 정보 조회
     *
     * @param email 현재 사용자의 이메일 정보
     * @return 현재 사용자의 프로필 정보 반환
     */
    @Override
    public ResponseProfile getProfile(String email) {

        User findUserProfile = userDomainService.findUserByEmail(email);

        return ResponseProfile.of(findUserProfile);
    }
}
