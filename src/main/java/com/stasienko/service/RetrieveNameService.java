package com.stasienko.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetrieveNameService {

    @Value("${IDENTITY_URL}")
    private String identityUrl;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public List<String> retrieveNameAndMail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId,
                    oauthToken.getName());

            String accessToken = client.getAccessToken().getTokenValue();

            return extractNameAndAddress(callIdentityEndpoint(accessToken));
        } else {
            System.out.println("Not Authenticated");
        }
        return null;
    }

    private String callIdentityEndpoint(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                identityUrl,
                HttpMethod.GET,
                entity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            System.err.println("Failed to fetch identity info. Status Code: " + response.getStatusCode());
            System.err.println("Response Body: " + response.getBody());
        }
        return null;
    }

    private List<String> extractNameAndAddress(String jsonResponse) {
        List<String> result = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            JsonNode payloadNode = rootNode.path("payload");
            String name = payloadNode.path("name").asText(null);

            JsonNode verifiableAddressesNode = rootNode.path("verifiable_addresses");
            String address = null;
            if (verifiableAddressesNode.isArray() && verifiableAddressesNode.size() > 0) {
                JsonNode firstAddressNode = verifiableAddressesNode.get(0);
                address = firstAddressNode.path("address").asText(null);
            }

            result.add(name);
            result.add(address);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
