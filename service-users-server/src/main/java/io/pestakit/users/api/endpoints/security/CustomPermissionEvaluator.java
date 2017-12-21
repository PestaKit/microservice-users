
package io.pestakit.users.api.endpoints.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object param, Object permission) {
        if (!(auth instanceof JwtAuthenticatedProfile) || !(param instanceof String) || !(permission instanceof String)){
            return false;
        }

        if(permission.equals("OWNER")) {
            return ((String)param).equals(((UserProfile)auth.getDetails()).getUserId().toString());
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
