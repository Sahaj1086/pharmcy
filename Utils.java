package com.medstore.utils;

public class Utils {

    // Method to validate email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Method to validate password strength
    public static boolean isValidPassword(String password) {
        return password.length() >= 8; // Example: Minimum 8 characters
    }

    // Method to format currency
    public static String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }

    // Method to check if a string is empty
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Method to generate a unique order ID (for example purposes)
    public static String generateOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
}