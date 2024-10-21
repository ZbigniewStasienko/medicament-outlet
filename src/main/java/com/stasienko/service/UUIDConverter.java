package com.stasienko.service;

import java.util.UUID;

public class UUIDConverter {

    public static UUID convertStringToUUID(String uuidString) {
        String cleanedUUIDString = uuidString.replaceAll("-", "");

        if (cleanedUUIDString.length() != 32) {
            System.out.println("Invalid UUID length: " + uuidString);
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
