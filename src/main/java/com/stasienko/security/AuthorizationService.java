package com.stasienko.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public class AuthorizationService {

    public static boolean isSuperAdmin(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return false;
        }

        List<String> roles = principal.getAttribute("roles");
        return roles != null && roles.contains("superadmin");
    }

    public static boolean isPharmacy(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return false;
        }

        String type = principal.getAttribute("type");
        return type != null && type.equals("pharmacy") && !isSuperAdmin(principal);
    }

}
