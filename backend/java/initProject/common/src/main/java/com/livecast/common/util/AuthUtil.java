package com.livecast.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtil {
    public static String getUsername(Authentication authentication){
        return ((Jwt) (authentication.getPrincipal()))
                .getClaims()
                .get("username")
                .toString();
    }
}
