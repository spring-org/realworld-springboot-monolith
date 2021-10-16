package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.persistence.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class RequestSaveUser {

    @NotEmpty(message = "email is not empty")
    private final String email;
    @NotEmpty(message = "userName is not empty")
    private final String userName;
    @NotEmpty(message = "password is not empty")
    private final String password;

    private RequestSaveUser(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public static RequestSaveUser of(String email, String userName, String password) {
        return new RequestSaveUser(email, userName, password);
    }

    public static User toEntity(RequestSaveUser saveUser, PasswordEncoder passwordEncoder) {
        return User.of(saveUser.getEmail(), passwordEncoder.encode(saveUser.getPassword()), saveUser.getUserName());
    }
}
