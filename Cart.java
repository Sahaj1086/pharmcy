package com.medstore.ui;

import com.medstore.models.CartItem;
import com.medstore.models.Medicine;
import com.medstore.models.User;
import com.medstore.services.CartService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Cart extends JFrame {
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton removeButton;
    private CartService cartService;
    private User currentUser;

    public Cart(User user) {
        this.currentUser = user;
        this.cartService = new CartService();
        initializeUI();
        loadCartItems();
    }

    private void initializeUI() {
        setTitle("🛒 Shopping Cart");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Medicine Name", "Quantity", "Price", "Total"}, 0);
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: ₹0.00");
        bottomPanel.add(totalLabel);

        removeButton = new JButton("Remove Item");
        removeButton.addActionListener(new RemoveItemAction());
        bottomPanel.add(removeButton);

        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new CheckoutAction());
        bottomPanel.add(checkoutButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        tableModel.setRowCount(0);
        List<CartItem> cartItems = cartService.getCartItems(currentUser.getId());
        double total = 0;

        for (CartItem item : cartItems) {
            Medicine medicine = cartService.getMedicineById(item.getMedicineId());
            if (medicine != null) {
                double itemTotal = medicine.getPrice() * item.getQuantity();
                total += itemTotal;
                tableModel.addRow(new Object[]{
                        medicine.getName(),
                        item.getQuantity(),
                        "₹" + medicine.getPrice(),
                        "₹" + itemTotal
                });
            }
        }

        totalLabel.setText("Total: ₹" + total);
    }

    private class RemoveItemAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                String medName = (String) tableModel.getValueAt(selectedRow, 0);
                int medicineId = findMedicineIdByName(medName);
                if (medicineId != -1) {
                    cartService.removeFromCart(currentUser.getId(), medicineId);
                    loadCartItems();
                }
            } else {
                JOptionPane.showMessageDialog(Cart.this, "Please select an item to remove.");
            }
        }
    }

    private class CheckoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(Cart.this, "🧾 Checkout complete!");
            cartService.clearCart(currentUser.getId());
            loadCartItems();
        }
    }

    private int findMedicineIdByName(String name) {
        // You can optimize later, for now fetch all items and match by name
        List<CartItem> items = cartService.getCartItems(currentUser.getId());
        for (CartItem item : items) {
            Medicine med = cartService.getMedicineById(item.getMedicineId());
            if (med != null && med.getName().equals(name)) {
                return med.getId();
            }
        }
        return -1;
    }
}
