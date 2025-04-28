package org.example.javafxdb_sql_shellcode;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

/**
 * A visual indicator for password strength
 */
public class PasswordStrengthIndicator {
    private final Label strengthLabel;
    private final PasswordField passwordField;
    private final StringProperty strengthDescription = new SimpleStringProperty();

    /**
     * Creates a new password strength indicator
     * @param passwordField The password field to monitor
     * @param strengthLabel The label to update with strength information
     */
    public PasswordStrengthIndicator(PasswordField passwordField, Label strengthLabel) {
        this.passwordField = passwordField;
        this.strengthLabel = strengthLabel;

        // Add listener to password field
        this.passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateStrength(newValue);
        });

        // Bind label text to strength description
        this.strengthLabel.textProperty().bind(strengthDescription);
    }

    /**
     * Updates the strength indicator based on password
     * @param password The password to evaluate
     */
    private void updateStrength(String password) {
        int score = calculatePasswordStrength(password);

        if (password.isEmpty()) {
            strengthDescription.set("");
            strengthLabel.setTextFill(Color.BLACK);
            return;
        }

        if (score < 40) {
            strengthDescription.set("Weak");
            strengthLabel.setTextFill(Color.RED);
        } else if (score < 70) {
            strengthDescription.set("Medium");
            strengthLabel.setTextFill(Color.ORANGE);
        } else if (score < 90) {
            strengthDescription.set("Strong");
            strengthLabel.setTextFill(Color.YELLOWGREEN);
        } else {
            strengthDescription.set("Very Strong");
            strengthLabel.setTextFill(Color.GREEN);
        }
    }

    /**
     * Calculates password strength score (0-100)
     * @param password The password to evaluate
     * @return Strength score (0-100)
     */
    private int calculatePasswordStrength(String password) {
        if (password.isEmpty()) return 0;

        int score = 0;

        // Length
        score += Math.min(password.length() * 5, 30);

        // Complexity factors
        if (password.matches(".*[A-Z].*")) score += 15; // Uppercase
        if (password.matches(".*[a-z].*")) score += 10; // Lowercase
        if (password.matches(".*[0-9].*")) score += 15; // Digits
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score += 20; // Special chars

        // Variety
        int uniqueChars = (int) password.chars().distinct().count();
        score += Math.min(uniqueChars * 2, 10);

        // Penalize common patterns
        if (password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$")) score -= 5; // Only alphanumeric
        if (password.toLowerCase().contains("password")) score -= 15;
        if (password.toLowerCase().contains("123")) score -= 10;
        if (password.length() < 8) score -= 20;

        return Math.max(0, Math.min(100, score));
    }
}