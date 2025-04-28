package org.example.javafxdb_sql_shellcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Manages user preferences including secure credential storage
 */
public class UserPreferences {
    private static final String PREF_NODE = "org/example/javafxdb_sql_shellcode";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD_HASH = "password_hash";
    private static final String PREF_SALT = "salt";
    private static final String PREF_THEME = "theme";
    private static final String PREF_LAST_LOGIN = "last_login";

    private static final Preferences prefs = Preferences.userRoot().node(PREF_NODE);

    /**
     * Saves user credentials securely
     * @param username The username
     * @param password The password
     * @throws NoSuchAlgorithmException If SHA-256 algorithm is not available
     */
    public static void saveCredentials(String username, String password) throws NoSuchAlgorithmException {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);

        // Hash the password with the salt
        String passwordHash = hashPassword(password, salt);

        // Save to preferences
        prefs.put(PREF_USERNAME, username);
        prefs.put(PREF_PASSWORD_HASH, passwordHash);
        prefs.put(PREF_SALT, saltBase64);
        prefs.put(PREF_LAST_LOGIN, String.valueOf(System.currentTimeMillis()));
    }

    /**
     * Validates user credentials
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    public static boolean validateCredentials(String username, String password) {
        try {
            String storedUsername = prefs.get(PREF_USERNAME, null);
            String storedPasswordHash = prefs.get(PREF_PASSWORD_HASH, null);
            String saltBase64 = prefs.get(PREF_SALT, null);

            if (storedUsername == null || storedPasswordHash == null || saltBase64 == null) {
                return false;
            }

            // Check username first
            if (!storedUsername.equals(username)) {
                return false;
            }

            // Get the salt
            byte[] salt = Base64.getDecoder().decode(saltBase64);

            // Hash the input password with the stored salt
            String inputPasswordHash = hashPassword(password, salt);

            // Compare the hashed password
            boolean isValid = storedPasswordHash.equals(inputPasswordHash);

            // Update last login timestamp if valid
            if (isValid) {
                prefs.put(PREF_LAST_LOGIN, String.valueOf(System.currentTimeMillis()));
            }

            return isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the UI theme preference
     * @param isDarkMode true if dark mode is enabled, false otherwise
     */
    public static void saveThemePreference(boolean isDarkMode) {
        prefs.putBoolean(PREF_THEME, isDarkMode);
    }

    /**
     * Gets the saved theme preference
     * @return true if dark mode is enabled, false otherwise
     */
    public static boolean isDarkModeEnabled() {
        return prefs.getBoolean(PREF_THEME, false);
    }

    /**
     * Hashes a password with a salt using SHA-256
     * @param password The password to hash
     * @param salt The salt to use
     * @return Base64 encoded hash
     * @throws NoSuchAlgorithmException If SHA-256 algorithm is not available
     */
    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     * Clears all saved preferences
     */
    public static void clearPreferences() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}