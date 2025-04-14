package com.example.rewear.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Classe utilitaire pour le hachage et la vérification des mots de passe
 */
public class PasswordUtils {

    /**
     * Hache un mot de passe avec SHA-256
     * @param password Mot de passe en clair
     * @return Le hachage en hexadécimal
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convertir le hash en représentation hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    /**
     * Vérifie si un mot de passe correspond à un hachage stocké
     * @param password Mot de passe en clair à vérifier
     * @param storedHash Hachage stocké à comparer
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean comparePassword(String password, String storedHash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(storedHash);
    }
}