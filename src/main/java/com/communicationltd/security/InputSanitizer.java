package com.communicationltd.security;

public class InputSanitizer {

    public static String sanitize(String input) {
        if (input == null) return "";

        return input.replace("<", "&lt;")
                .replace(">", "&gt;")
                .trim();
    }

    public static String normalize(String input) {
        return input == null ? "" : input.trim();
    }

    public static boolean isValidEmail(String email) {
        return email != null
                && email.length() <= 254
                && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidUsername(String username) {
        return username != null
                && username.matches("^[A-Za-z0-9_.-]{3,40}$");
    }

    public static boolean isReasonableText(String value, int maxLength) {
        return value != null && !value.isBlank() && value.length() <= maxLength;
    }
}
