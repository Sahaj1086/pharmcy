package com.medstore.ui;
import com.medstore.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class AdminDashboard extends JFrame {
    private JTable medicineTable;
    private JTextField nameField, categoryField, priceField, quantityField, expiryField, descriptionField;
    private JButton addButton, updateButton, deleteButton, refreshButton, logoutButton;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // === Top Input Panel ===
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 8, 8));

        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        expiryField = new JTextField("YYYY-MM-DD");
        descriptionField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Expiry Date:"));
        inputPanel.add(expiryField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        add(inputPanel, BorderLayout.NORTH);

        // === Table ===
        medicineTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        add(scrollPane, BorderLayout.CENTER);

        // === Buttons ===
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // === Load Table ===
        loadMedicines();

        // === Table Row Click ===
        medicineTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = medicineTable.getSelectedRow();
                if (row != -1) {
                    nameField.setText(medicineTable.getValueAt(row, 1).toString());
                    categoryField.setText(medicineTable.getValueAt(row, 2).toString());
                    priceField.setText(medicineTable.getValueAt(row, 3).toString());
                    quantityField.setText(medicineTable.getValueAt(row, 4).toString());
                    expiryField.setText(medicineTable.getValueAt(row, 5).toString());
                    descriptionField.setText(medicineTable.getValueAt(row, 6).toString());
                }
            }
        });

        // === Button Actions ===
        addButton.addActionListener(e -> addMedicine());
        updateButton.addActionListener(e -> updateMedicine());
        deleteButton.addActionListener(e -> deleteMedicine());
        refreshButton.addActionListener(e -> loadMedicines());
        logoutButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // === Load Medicines from DB ===
    private void loadMedicines() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medicines")) {

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID", "Name", "Category", "Price", "Quantity", "Expiry Date", "Description"}, 0
            );

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("category"));
                row.add(rs.getDouble("price"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDate("expiry_date"));
                row.add(rs.getString("description"));
                model.addRow(row);
            }

            medicineTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading medicines:\n" + e.getMessage());
        }
    }

    // === Add Medicine ===
    private void addMedicine() {
        if (!validateFields()) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO medicines (name, category, price, quantity, expiry_date, description) VALUES (?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, categoryField.getText());
            pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
            pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
            pstmt.setDate(5, Date.valueOf(LocalDate.parse(expiryField.getText())));
            pstmt.setString(6, descriptionField.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Medicine added successfully!");
            clearFields();
            loadMedicines();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error adding medicine:\n" + e.getMessage());
        }
    }

    // === Update Medicine ===
    private void updateMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to update.");
            return;
        }

        if (!validateFields()) return;
        int id = (int) medicineTable.getValueAt(row, 0);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE medicines SET name=?, category=?, price=?, quantity=?, expiry_date=?, description=? WHERE id=?")) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, categoryField.getText());
            pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
            pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
            pstmt.setDate(5, Date.valueOf(LocalDate.parse(expiryField.getText())));
            pstmt.setString(6, descriptionField.getText());
            pstmt.setInt(7, id);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Medicine updated successfully!");
            clearFields();
            loadMedicines();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error updating medicine:\n" + e.getMessage());
        }
    }

    // === Delete Medicine ===
    private void deleteMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete.");
            return;
        }

        int id = (int) medicineTable.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this medicine?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM medicines WHERE id=?")) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "🗑 Medicine deleted successfully!");
            loadMedicines();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error deleting medicine:\n" + e.getMessage());
        }
    }

    // === Validation ===
    private boolean validateFields() {
        if (nameField.getText().isEmpty() || categoryField.getText().isEmpty() ||
                priceField.getText().isEmpty() || quantityField.getText().isEmpty() ||
                expiryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return false;
        }
        try {
            Double.parseDouble(priceField.getText());
            Integer.parseInt(quantityField.getText());
            LocalDate.parse(expiryField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data format. Check price, quantity, or expiry date (YYYY-MM-DD).");
            return false;
        }
        return true;
    }

    // === Clear Input Fields ===
    private void clearFields() {
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        expiryField.setText("YYYY-MM-DD");
        descriptionField.setText("");
    }

    // === Main Method for Testing ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
