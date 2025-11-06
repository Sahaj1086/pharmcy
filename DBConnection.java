package com.medstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/medstore_db";
    private static final String USER = "root"; // ✅ just "root"
    private static final String PASSWORD = "vishwa@1086"; // ✅ or your real password if you set one

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found. Please add it to the lib folder.");
        } catch (SQLException e) {
            System.err.println("⚠️ Database connection failed: " + e.getMessage());
        }
        return null; // return null instead of throwing exception
    }
}
