package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveUser {

    @NotEmpty(message = "email is not empty")
    private String email;
    @NotEmpty(message = "userName is not empty")
    private String userName;
    @NotEmpty(message = "password is not empty")
    private String password;

    private RequestSaveUser(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public static RequestSaveUser of(String email, String userName, String password) {
        return new RequestSaveUser(email, userName, password);
    }

    public static User toEntity(RequestSaveUser saveUser, PasswordEncoder passwordEncoder) {
        return UserFactory.of(saveUser.getEmail(), passwordEncoder.encode(saveUser.getPassword()), saveUser.getUserName());
    }
}
