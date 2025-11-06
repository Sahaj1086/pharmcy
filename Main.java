package com.medstore;

import javax.swing.SwingUtilities;
import com.medstore.ui.Login;

public class Main {
    public static void main(String[] args) {
        System.out.println("---- MedStore App Starting ----");

        try (java.sql.Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ DB connection OK");
        } catch (Exception ex) {
            System.err.println("⚠️ DB connection failed: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🟢 Launching Login window...");
                Login loginWindow = new Login();
                loginWindow.setVisible(true);
                System.out.println("✅ Login window should now be visible.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
