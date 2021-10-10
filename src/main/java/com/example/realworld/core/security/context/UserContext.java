package com.example.realworld.core.security.context;

import com.example.realworld.application.users.persistence.User;
import lombok.Generated;
import lombok.Getter;

import java.util.Objects;


public final class UserContext {

    @Getter
    private final User user;

    public UserContext(User user) {
        this.user = user;
    }

    public static UserContext of(User user) {
        return new UserContext(user);
    }

    public String email() {
        return user.getEmail();
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserContext)) return false;
        UserContext that = (UserContext) o;
        return Objects.equals(user, that.user);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
