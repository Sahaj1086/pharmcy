package com.medstore.ui;

import com.medstore.DBConnection;  // ✅ Make sure this import is present

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OrderHistory extends JFrame {
    private JTable orderTable;
    private JButton backButton;
    private int userId;

    public OrderHistory(int userId) {
        this.userId = userId;
        setTitle("Order History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
        loadOrderHistory();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new BorderLayout());

        orderTable = new JTable(new DefaultTableModel(new Object[]{"Order ID", "Total Amount", "Order Date"}, 0));
        JScrollPane scrollPane = new JScrollPane(orderTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserDashboard(userId).setVisible(true);  // ✅ Works if your dashboard accepts userId
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadOrderHistory() {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String query = "SELECT id, total_amount, order_date FROM orders WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDouble("total_amount"),
                        rs.getDate("order_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
