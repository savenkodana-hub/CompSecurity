package com.communicationltd.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateToken(String email) {
        try {
            byte[] randomBytes = new byte[32];
            SECURE_RANDOM.nextBytes(randomBytes);

            String raw = email + ":" + System.nanoTime() + ":" + Base64.getEncoder().encodeToString(randomBytes);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));

            return toHex(hash);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashToken(String email, String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest((email + ":" + token).getBytes(StandardCharsets.UTF_8));
            return toHex(hash);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
