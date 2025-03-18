package org.universeapps.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TokenProvider {
    private static volatile String token;

    public static String getToken() {
        if (token == null) {
            synchronized (TokenProvider.class) {
                if (token == null) {
                    try {
                        token = new String(Files.readAllBytes(Paths.get("token.txt"))).trim();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load token from token.txt", e);
                    }
                }
            }
        }
        return token;
    }
}
