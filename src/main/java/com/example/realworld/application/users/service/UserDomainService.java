package com.example.realworld.application.users.service;

import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.core.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    /**
     * 특정 사용자 조회
     *
     * @param userEmail 특정 사용자의 이메일 정보
     * @return 조회된 사용자의 정보를 반환
     */
    @Transactional(readOnly = true)
    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(NotFoundUserException::new);
    }

    /**
     * 특정 사용자의 존재유무
     *
     * @param userEmail 특정 사용자의 이메일 정보
     * @return 특정 사용자의 존재유무 확인
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    /**
     * 새로운 사용자 등록
     *
     * @param userEntity 새로 등록하기 위한 사용자 정보
     * @return 등록된 사용자 정보를 반환
     */
    @Transactional
    public User save(User userEntity) {
        return userRepository.save(userEntity);
    }
}
