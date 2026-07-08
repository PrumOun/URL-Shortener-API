package com.odev.urlshortener.demo.util;

import java.security.SecureRandom;

public class ShortCodeGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int SHORT_CODE_LENGTH = 7;
    public static final SecureRandom RANDOM = new SecureRandom();

    private ShortCodeGenerator() {
        // Private constructor to prevent instantiation
    }

    public static String generate(){
        return generate(SHORT_CODE_LENGTH);
    }

    public static String generate(int length) {
        StringBuilder shortCode = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            shortCode.append(ALPHABET.charAt(index));
        }
        return shortCode.toString();
    }
}
