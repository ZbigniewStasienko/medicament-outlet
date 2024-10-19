package com.stasienko.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        boolean isAdmin = AuthorizationService.isSuperAdmin(oauthUser);
        boolean isPharmacy = AuthorizationService.isPharmacy(oauthUser);

        if (isAdmin) {
            response.sendRedirect("/admin");
        } else if (isPharmacy) {
            response.sendRedirect("/pharmacy");
        }else {
            response.sendRedirect("/");
        }
    }
}
