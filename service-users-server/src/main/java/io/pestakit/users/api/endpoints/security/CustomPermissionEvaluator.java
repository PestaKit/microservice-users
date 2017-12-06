
package io.pestakit.users.api.endpoints.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object param, Object permission) {
        System.out.println("handled");
        if (!(auth instanceof JwtAuthenticatedProfile) || !(param instanceof Long) || !(permission instanceof String)){
            return false;
        }

        if(permission.equals("OWNER")) {
            return (long)param == ((JwtAuthenticatedProfile)auth).getProfile().getUserId();
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
