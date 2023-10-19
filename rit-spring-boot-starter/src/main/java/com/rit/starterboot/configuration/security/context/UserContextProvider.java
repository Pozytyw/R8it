package com.rit.starterboot.configuration.security.context;

import com.rit.starterboot.domain.user.UserContext;

public class UserContextProvider {

    private final UserContext userContext;

    public UserContextProvider(UserContext userContext) {
        this.userContext = userContext;
    }

    public UserContext get() {
        if (userContext == null) {
            throw new IllegalStateException("User context must be not null");
        }

        return userContext;
    }
}
