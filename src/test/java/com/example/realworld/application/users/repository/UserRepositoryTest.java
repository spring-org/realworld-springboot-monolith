package com.example.realworld.application.users.repository;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User savedUser;

    private User createUser(Long id, String email) {
        return User.registeredUser(id, email, "1234");
    }

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(createUser(1L, "seokrae@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("사용자 등록 확인 테스트")
    @Test
    void when_createUser_expected_exist() {
        // when
        User findUser = userRepository.findByEmail("seokrae@gmail.com")
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        // then
        assertThat(findUser.getEmail()).isEqualTo("seokrae@gmail.com");
    }

    @DisplayName("사용자 수정 테스트")
    @Test
    void when_updateUser_expected_changeInfo() {
        // when
        savedUser.updateUser(null, null, "username", "bio", "gmail.com/image.png");
        userRepository.flush();

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // then
        assertThat(findUser.getProfile().getImage()).isNotNull();
    }

    @DisplayName("사용자 수정 요청 데이터 없는 경우의 테스트")
    @Test
    void when_updatedUser_expected_noData_changeInfo() {
        // when
        savedUser.updateUser(null, null, null, null, null);
        userRepository.flush();

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // then
        assertThat(findUser.getProfile().getImage()).isNull();
    }

    @DisplayName("사용자 간의 Follow 테스트")
    @Test
    void when_follow_expected_following() {
        // given
        User toUser = createUser(2L, "seok@gmail.com");

        savedUser.addFollower(toUser);
        userRepository.flush();

        // when
        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        boolean contains = findUser.isFollowing(toUser);
        // then
        assertThat(contains).isTrue();
    }

    @DisplayName("사용자 간의 Not Follow 테스트")
    @Test
    void when_follow_expected_not_follow() {
        // given
        User toUser = createUser(2L, "seok@gmail.com");

        // when
        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        boolean contains = findUser.isFollowing(toUser);
        // then
        assertThat(contains).isFalse();
    }


    @DisplayName("팔로우 된 기존 사용자의 언팔로우 테스트")
    @Test
    void when_unFollowing_expected_not_follow() {
        // given
        User toUser = createUser(2L, "seok@gmail.com");

        savedUser.addFollower(toUser); // 팔로우

        userRepository.flush();
        // when
        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        findUser.unFollow(toUser); // 언팔

        boolean expected = findUser.isFollowing(toUser);

        // then
        assertThat(expected).isFalse();
    }

    @DisplayName("부모 테이블의 레코드에 따라 하위 테이블의 데이터 삭제 테스트(쿼리 확인)")
    @Test
    void when_deleteUser_expected_deleteFollow_cascade() {
        // given
        User toUser = createUser(2L, "seok@gmail.com");
        User toUser2 = createUser(3L, "seok2@gmail.com");
        User toUser3 = createUser(4L, "seok3@gmail.com");

        List<User> saveUsers = List.of(toUser, toUser2, toUser3);
        userRepository.saveAll(saveUsers);

        savedUser.addFollower(toUser); // 팔로우
        savedUser.addFollower(toUser2); // 팔로우
        savedUser.addFollower(toUser3); // 팔로우
        userRepository.flush();

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // when
        userRepository.delete(findUser);
        userRepository.flush();

        int size = userRepository.findAll().size();
        assertThat(size).isEqualTo(3);
    }
}