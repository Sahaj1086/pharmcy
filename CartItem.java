package com.medstore.models;

public class CartItem {
    private int id;
    private int userId;
    private int medicineId;
    private int quantity;
    private String medicineName; // ✅ for display
    private double price;        // ✅ for calculating total

    public CartItem() {}

    public CartItem(int id, int userId, int medicineId, int quantity, String medicineName, double price) {
        this.id = id;
        this.userId = userId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.medicineName = medicineName;
        this.price = price;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // ✅ Optional helper for UI display
    public double getTotalPrice() {
        return price * quantity;
    }
}
