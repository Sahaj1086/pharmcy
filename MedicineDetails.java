package com.medstore.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import com.medstore.DBConnection;

public class MedicineDetails extends JFrame {
    private JLabel nameLabel, categoryLabel, priceLabel, quantityLabel, expiryLabel, descriptionLabel;
    private JButton addToCartButton;
    private int medicineId;

    public MedicineDetails(int medicineId) {
        this.medicineId = medicineId;
        setTitle("Medicine Details");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        nameLabel = new JLabel();
        categoryLabel = new JLabel();
        priceLabel = new JLabel();
        quantityLabel = new JLabel();
        expiryLabel = new JLabel();
        descriptionLabel = new JLabel();
        addToCartButton = new JButton("Add to Cart");

        add(new JLabel("Name:"));
        add(nameLabel);
        add(new JLabel("Category:"));
        add(categoryLabel);
        add(new JLabel("Price:"));
        add(priceLabel);
        add(new JLabel("Quantity:"));
        add(quantityLabel);
        add(new JLabel("Expiry Date:"));
        add(expiryLabel);
        add(new JLabel("Description:"));
        add(descriptionLabel);
        add(new JLabel());
        add(addToCartButton);

        loadMedicineDetails();

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
    }

    private void loadMedicineDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM medicines WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                categoryLabel.setText(rs.getString("category"));
                priceLabel.setText(String.valueOf(rs.getDouble("price")));
                quantityLabel.setText(String.valueOf(rs.getInt("quantity")));
                expiryLabel.setText(rs.getString("expiry_date"));
                descriptionLabel.setText(rs.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToCart() {
        JOptionPane.showMessageDialog(this, "Medicine added to cart!");
    }
}
