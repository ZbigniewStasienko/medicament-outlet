package com.stasienko.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.UUID;

public class UUIDConverter {

    public static UUID convertToUUID(OAuth2User principal) {
        String inputId = principal.getAttribute("user_id");
        assert inputId != null;
        return convertStringToUUID(inputId);
    }

    public static UUID convertStringToUUID(String inputId) {
        String cleanedUUIDString = inputId.replaceAll("-", "");

        if (cleanedUUIDString.length() != 32) {
            System.out.println("Invalid UUID length: " + inputId);
            return null;
        }

        String formattedUUIDString = cleanedUUIDString.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                "$1-$2-$3-$4-$5"
        );

        try {
            UUID uuid = UUID.fromString(formattedUUIDString);
            return uuid;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format after inserting hyphens: " + formattedUUIDString);
            return null;
        }
    }
}
