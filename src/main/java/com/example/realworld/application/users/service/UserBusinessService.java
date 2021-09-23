package com.example.realworld.application.users.service;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBusinessService implements UserService {

    private final UserRepository userRepository;

    // 사용자 저장
    @Transactional
    @Override
    public ResponseUser addUser(final RequestSaveUser saveUser) {
        boolean existsUser = userRepository.existsByEmail(saveUser.getEmail());

        if (existsUser) {
            throw new DuplicateUserException("이미 존재하는 사용자입니다.");
        }

        User savedUser = userRepository.save(RequestSaveUser.toEntity(saveUser));
        return ResponseUser.of(savedUser);
    }

    // 사용자 정보 수정
    @Transactional
    @Override
    public ResponseUser updateUser(String email, RequestUpdateUser updateUser) {

        User user = findUserByEmail(email);
        user.update(updateUser);

        return ResponseUser.of(user);
    }

    @Override
    public ResponseUser getUserByEmail(String email) {

        User findUser = findUserByEmail(email);

        return ResponseUser.of(findUser);
    }

    // 사용자 - 프로필 조회
    @Override
    public ResponseProfile getProfile(String email) {

        User findUserProfile = findUserByEmail(email);

        return ResponseProfile.of(findUserProfile);
    }

    @Override
    public boolean existsUserByEmail(String email) {

        User findUser = findUserByEmail(email);

        return !Objects.isNull(findUser);
    }

    // 사용자 조회
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }
}
