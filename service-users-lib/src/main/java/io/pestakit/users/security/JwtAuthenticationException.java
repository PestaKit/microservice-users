
package io.pestakit.users.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String message, Exception e) {
        super(message + " : " + e.getMessage());
    }
}
