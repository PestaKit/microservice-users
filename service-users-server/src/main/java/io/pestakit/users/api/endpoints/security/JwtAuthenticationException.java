
package io.pestakit.users.api.endpoints.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String message, Exception e) {
        super(message + " : " + e.getMessage());
    }
}
