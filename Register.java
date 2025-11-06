package com.medstore.ui;

import com.medstore.models.User;
import com.medstore.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton registerButton, loginButton;
    private UserService userService;

    public Register() {
        userService = new UserService();

        setTitle("Register - MedStore");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField();
        add(nameLabel);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        add(emailLabel);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        add(roleLabel);
        add(roleBox);

        registerButton = new JButton("Register");
        loginButton = new JButton("Already have an account? Login");
        add(registerButton);
        add(loginButton);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = (String) roleBox.getSelectedItem();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required.");
                    return;
                }

                if (userService.isEmailExists(email)) {
                    JOptionPane.showMessageDialog(null, "Email already exists. Try logging in.");
                    return;
                }

                User newUser = new User(0, name, email, password, role);

                if (userService.registerUser(newUser)) {
                    JOptionPane.showMessageDialog(null, "Registration successful! You can now log in.");
                    dispose();
                    new Login().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Try again.");
                }
            }
        });

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}
