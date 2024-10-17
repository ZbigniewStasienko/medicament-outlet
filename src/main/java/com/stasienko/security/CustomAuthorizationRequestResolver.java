package com.stasienko.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    enum IDP {
        USERS, PHARMACIES
    }

    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);
        IDP requestedIDP = IDP.USERS;
        if (request.getQueryString() != null && request.getQueryString().equals("idp=pharmacies")) {
            requestedIDP = IDP.PHARMACIES;
        }

        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest, requestedIDP) : null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        IDP requestedIDP = IDP.USERS;
        if (request.getQueryString() != null && request.getQueryString().equals("idp=pharmacies")) {
            requestedIDP = IDP.PHARMACIES;
        }

        OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId);
        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest, requestedIDP) : null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, IDP requestedIDP) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
        switch (requestedIDP) {
            case USERS -> additionalParameters.put("idp_hint", "8f95d4d853ef4588a475c65c97f70019");
            case PHARMACIES -> additionalParameters.put("idp_hint", "639a76563b304a3da2412308987a7025");
        }
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }
}