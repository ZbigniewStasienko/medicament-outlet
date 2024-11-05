package com.stasienko.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${LOGOUT}")
    private String logoutAddress;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        request.getSession().invalidate();

        String redirectUri = URLEncoder.encode("http://localhost:8080/", StandardCharsets.UTF_8);
        String logoutUrl = logoutAddress + "?redirect_uri=" + redirectUri;
        response.sendRedirect(logoutUrl);
    }
}
