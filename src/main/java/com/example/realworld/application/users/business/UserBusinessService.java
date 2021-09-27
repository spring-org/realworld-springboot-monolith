package com.example.realworld.application.users.business;

import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserDomainService;
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

    // 사용자 저장
    @Transactional
    @Override
    public ResponseUser addUser(final RequestSaveUser saveUser) {
        boolean existsUser = userDomainService.existsByEmail(saveUser.getEmail());

        if (existsUser) {
            throw new DuplicateUserException("이미 존재하는 사용자입니다.");
        }

        User savedUser = userDomainService.save(RequestSaveUser.toEntity(saveUser));
        return ResponseUser.of(savedUser);
    }

    // 사용자 정보 수정
    @Transactional
    @Override
    public ResponseUser updateUser(String email, RequestUpdateUser updateUser) {

        User user = userDomainService.findUserByEmail(email);
        user.update(updateUser);

        return ResponseUser.of(user);
    }

    @Override
    public ResponseUser getUserByEmail(String email) {

        User findUser = userDomainService.findUserByEmail(email);

        return ResponseUser.of(findUser);
    }

    // 사용자 - 프로필 조회
    @Override
    public ResponseProfile getProfile(String email) {

        User findUserProfile = userDomainService.findUserByEmail(email);

        return ResponseProfile.of(findUserProfile);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userDomainService.existsByEmail(email);
    }
}