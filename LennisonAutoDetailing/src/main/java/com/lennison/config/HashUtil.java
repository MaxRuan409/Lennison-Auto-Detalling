package com.lennison.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utilidades para hashear contraseñas usando SHA-256.
 */
public class HashUtil {

    /**
     * Hashea un texto plano usando SHA-256.
     */
    public static String hashear(String textoPlano) {
        if (textoPlano == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(textoPlano.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error grave: SHA-256 no disponible.");
            throw new RuntimeException("Error al hashear", e);
        }
    }
}
