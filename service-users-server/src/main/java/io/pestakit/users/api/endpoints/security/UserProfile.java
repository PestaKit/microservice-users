
package io.pestakit.users.api.endpoints.security;

import java.util.UUID;

public class UserProfile {
    private UUID userId;
    private String username;

    public UserProfile(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

}
