package com.stasienko.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    RequestCache requestCache = new HttpSessionRequestCache();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .requestCache(cache -> cache.requestCache(requestCache))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/", "/pictures/**", "/admin", "/favicon.ico", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(req -> req.authorizationEndpoint(res -> res.authorizationRequestResolver(
                        new CustomAuthorizationRequestResolver(this.clientRegistrationRepository)))
                        .successHandler(successHandler) );
        return http.build();
    }
}



