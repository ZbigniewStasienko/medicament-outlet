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

    public static boolean isUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return false;
        }
        String idp = principal.getAttribute("idp");
        return idp != null && idp.equals("8f95d4d853ef4588a475c65c97f70019");
    }

}
