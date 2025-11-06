package com.medstore.services;

import com.medstore.models.Medicine;
import com.medstore.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicineService {

    private Connection connection;

    public MedicineService() {
        try {
            connection = DBConnection.getConnection();
            if (connection == null) {
                System.out.println("⚠️ Database connection is NULL — check DBConnection settings!");
            } else {
                System.out.println("✅ MedicineService connected to database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Add Medicine
    public void addMedicine(Medicine medicine) {
        if (connection == null) {
            System.err.println("❌ Cannot add medicine — No DB connection!");
            return;
        }
        String query = "INSERT INTO medicines (name, category, price, quantity, expiry_date, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, medicine.getName());
            statement.setString(2, medicine.getCategory());
            statement.setDouble(3, medicine.getPrice());
            statement.setInt(4, medicine.getQuantity());

            // ✅ Safe date conversion
            if (medicine.getExpiryDate() != null) {
                statement.setDate(5, Date.valueOf(medicine.getExpiryDate()));
            } else {
                statement.setDate(5, null);
            }

            statement.setString(6, medicine.getDescription());
            statement.executeUpdate();
            System.out.println("✅ Medicine added successfully");
        } catch (SQLException e) {
            System.err.println("❌ SQL error while adding medicine: " + e.getMessage());
        }
    }

    // ✅ Update Medicine
    public void updateMedicine(Medicine medicine) {
        if (connection == null) {
            System.err.println("❌ Cannot update — No DB connection!");
            return;
        }
        String query = "UPDATE medicines SET name=?, category=?, price=?, quantity=?, expiry_date=?, description=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, medicine.getName());
            statement.setString(2, medicine.getCategory());
            statement.setDouble(3, medicine.getPrice());
            statement.setInt(4, medicine.getQuantity());
            if (medicine.getExpiryDate() != null)
                statement.setDate(5, Date.valueOf(medicine.getExpiryDate()));
            else
                statement.setDate(5, null);
            statement.setString(6, medicine.getDescription());
            statement.setInt(7, medicine.getId());
            statement.executeUpdate();
            System.out.println("✅ Medicine updated successfully");
        } catch (SQLException e) {
            System.err.println("❌ SQL error while updating medicine: " + e.getMessage());
        }
    }

    // ✅ Delete Medicine
    public void deleteMedicine(int id) {
        if (connection == null) {
            System.err.println("❌ Cannot delete — No DB connection!");
            return;
        }
        String query = "DELETE FROM medicines WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("🗑️ Medicine deleted successfully");
        } catch (SQLException e) {
            System.err.println("❌ SQL error while deleting medicine: " + e.getMessage());
        }
    }

    // ✅ Fetch All Medicines
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        if (connection == null) {
            System.err.println("❌ Cannot load medicines — No DB connection!");
            return medicines;
        }

        String query = "SELECT * FROM medicines";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Medicine med = new Medicine();
                med.setId(rs.getInt("id"));
                med.setName(rs.getString("name"));
                med.setCategory(rs.getString("category"));
                med.setPrice(rs.getDouble("price"));
                med.setQuantity(rs.getInt("quantity"));

                Date expiry = rs.getDate("expiry_date");
                med.setExpiryDate(expiry != null ? expiry.toLocalDate() : LocalDate.now());

                med.setDescription(rs.getString("description"));
                medicines.add(med);
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL error while fetching medicines: " + e.getMessage());
        }
        return medicines;
    }

    // ✅ Fetch Single Medicine by ID
    public Medicine getMedicineById(int id) {
        Medicine med = null;
        if (connection == null) {
            System.err.println("❌ Cannot fetch by ID — No DB connection!");
            return null;
        }

        String query = "SELECT * FROM medicines WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                med = new Medicine();
                med.setId(rs.getInt("id"));
                med.setName(rs.getString("name"));
                med.setCategory(rs.getString("category"));
                med.setPrice(rs.getDouble("price"));
                med.setQuantity(rs.getInt("quantity"));
                Date expiry = rs.getDate("expiry_date");
                med.setExpiryDate(expiry != null ? expiry.toLocalDate() : LocalDate.now());
                med.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL error while fetching medicine: " + e.getMessage());
        }
        return med;
    }

    // ✅ Search Medicines by Name or Category
    public List<Medicine> searchMedicines(String keyword) {
        List<Medicine> medicines = new ArrayList<>();
        if (connection == null) {
            System.err.println("❌ Cannot search — No DB connection!");
            return medicines;
        }

        String query = "SELECT * FROM medicines WHERE name LIKE ? OR category LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medicine med = new Medicine();
                med.setId(rs.getInt("id"));
                med.setName(rs.getString("name"));
                med.setCategory(rs.getString("category"));
                med.setPrice(rs.getDouble("price"));
                med.setQuantity(rs.getInt("quantity"));
                Date expiry = rs.getDate("expiry_date");
                med.setExpiryDate(expiry != null ? expiry.toLocalDate() : LocalDate.now());
                med.setDescription(rs.getString("description"));
                medicines.add(med);
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL error while searching: " + e.getMessage());
        }
        return medicines;
    }
}
