package com.medstore.services;

import com.medstore.DBConnection;
import com.medstore.models.CartItem;
import com.medstore.models.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    // ✅ Add item or update quantity
    public void addToCart(int userId, int medicineId, int quantity) {
        String query = "INSERT INTO cart (user_id, medicine_id, quantity) VALUES (?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, medicineId);
            statement.setInt(3, quantity);
            statement.setInt(4, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Remove item
    public void removeFromCart(int userId, int medicineId) {
        String query = "DELETE FROM cart WHERE user_id = ? AND medicine_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, medicineId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Fetch all cart items for a user
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT c.id, c.user_id, c.medicine_id, c.quantity, m.name, m.price " +
                       "FROM cart c JOIN medicines m ON c.medicine_id = m.id WHERE c.user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setMedicineId(rs.getInt("medicine_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setMedicineName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    // ✅ Clear the cart after checkout
    public void clearCart(int userId) {
        String query = "DELETE FROM cart WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Get medicine details by ID
    public Medicine getMedicineById(int medicineId) {
        String query = "SELECT * FROM medicines WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, medicineId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Medicine med = new Medicine();
                med.setId(rs.getInt("id"));
                med.setName(rs.getString("name"));
                med.setCategory(rs.getString("category"));
                med.setPrice(rs.getDouble("price"));
                med.setQuantity(rs.getInt("quantity"));
                java.sql.Date expiryDate = rs.getDate("expiry_date");
                if (expiryDate != null) {
                    med.setExpiryDate(expiryDate.toLocalDate());
                }
                med.setDescription(rs.getString("description"));
                return med;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
