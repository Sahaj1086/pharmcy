package com.medstore.ui;

import com.medstore.DBConnection;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public Login() {
        setTitle("MedStore - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 50, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 50, 180, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(260, 150, 100, 30);
        add(registerButton);

        // Login Button Action
        loginButton.addActionListener(e -> loginUser());

        // Register Button Action
        registerButton.addActionListener(e -> {
            new Register().setVisible(true);
            dispose();
        });
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                JOptionPane.showMessageDialog(this, "Login successful!");

                // Open dashboard based on role
                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new UserDashboard(userId).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}
