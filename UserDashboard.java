package com.medstore.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import com.medstore.models.Medicine;
import com.medstore.services.MedicineService;

public class UserDashboard extends JFrame {
    private int userId;
    private JTextField searchField;
    private JPanel medicineCardsPanel;
    private MedicineService medicineService;
    private JButton viewCartButton, viewOrdersButton, logoutButton;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(0, 121, 193);
    private final Color SECONDARY_COLOR = new Color(23, 162, 184);
    private final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private final Color ACCENT_COLOR = new Color(255, 193, 7);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private final Color DISCOUNT_COLOR = new Color(220, 53, 69);

    public UserDashboard(int userId) {
        this.userId = userId;
        medicineService = new MedicineService();
        
        setTitle("MedKart - India's Trusted Online Pharmacy");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));

        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main Content
        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadMedicines();
        setupButtonActions();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(12, 30, 12, 30)
        ));

        // Logo and Location
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel logoLabel = new JLabel("⚕");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 36));
        logoLabel.setForeground(PRIMARY_COLOR);
        
        JLabel brandLabel = new JLabel("medkart");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        brandLabel.setForeground(PRIMARY_COLOR);
        
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        locationPanel.setBackground(Color.WHITE);
        JLabel locationIcon = new JLabel("📍");
        locationIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel locationText = new JLabel("<html>Delivering to<br/><b>Enter pincode ▼</b></html>");
        locationText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        locationText.setForeground(TEXT_PRIMARY);
        locationPanel.add(locationIcon);
        locationPanel.add(locationText);
        
        leftPanel.add(logoLabel);
        leftPanel.add(brandLabel);
        leftPanel.add(locationPanel);

        // Search Bar
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(40);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
            new EmptyBorder(10, 15, 10, 45)
        ));
        
        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchButton.setBorder(null);
        searchButton.setContentAreaFilled(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> searchMedicines());
        
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new OverlayLayout(searchPanel));
        searchPanel.setBackground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 7));
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchButton);
        
        searchPanel.add(buttonPanel);
        searchPanel.add(searchField);
        
        centerPanel.add(searchPanel);

        // Right Panel - Actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        
        viewCartButton = createIconButton("🛒", "Cart");
        viewOrdersButton = createIconButton("📦", "Orders");
        logoutButton = createIconButton("👤", "Sign in");
        
        rightPanel.add(viewCartButton);
        rightPanel.add(viewOrdersButton);
        rightPanel.add(logoutButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Quick Actions Section
        mainPanel.add(createQuickActionsSection());
        mainPanel.add(Box.createVerticalStrut(30));

        // Stats Section
        mainPanel.add(createStatsSection());
        mainPanel.add(Box.createVerticalStrut(30));

        // Medicines Section
        JPanel medicinesHeader = new JPanel(new BorderLayout());
        medicinesHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        medicinesHeader.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Frequently Purchased");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JButton viewAllButton = new JButton("View All >");
        viewAllButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        viewAllButton.setForeground(PRIMARY_COLOR);
        viewAllButton.setBorder(null);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        medicinesHeader.add(titleLabel, BorderLayout.WEST);
        medicinesHeader.add(viewAllButton, BorderLayout.EAST);
        mainPanel.add(medicinesHeader);
        
        mainPanel.add(Box.createVerticalStrut(20));

        // Medicine Cards Grid
        medicineCardsPanel = new JPanel(new GridLayout(0, 5, 20, 20));
        medicineCardsPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(medicineCardsPanel);

        return mainPanel;
    }

    private JPanel createQuickActionsSection() {
        JPanel section = new JPanel(new GridLayout(1, 3, 20, 0));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        section.setBackground(BACKGROUND_COLOR);

        // Upload Prescription Card
        section.add(createActionCard("📋", "Order Medicine With Prescription", "Upload Now", 
            new Color(230, 245, 255)));

        // WhatsApp Order Card
        section.add(createActionCard("💬", "Order Medicine On Whatsapp!", "@+91- 7861804725", 
            new Color(220, 250, 235)));

        // Refill Medicine Card
        section.add(createActionCard("📝", "Refill Your Medicine Order", "Re-Order", 
            new Color(245, 235, 255)));

        return section;
    }

    private JPanel createActionCard(String emoji, String title, String buttonText, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(bgColor);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JButton actionButton = new JButton(buttonText);
        actionButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionButton.setForeground(PRIMARY_COLOR);
        actionButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            new EmptyBorder(6, 15, 6, 15)
        ));
        actionButton.setContentAreaFilled(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(actionButton);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatsSection() {
        JPanel section = new JPanel(new GridLayout(1, 4, 30, 0));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        section.setBackground(BACKGROUND_COLOR);

        section.add(createStatCard("🏪", "100+ Stores", "Across India"));
        section.add(createStatCard("👥", "12 Lakh", "Happy Customer"));
        section.add(createStatCard("💰", "Upto 85%", "Savings"));
        section.add(createStatCard("🚚", "Same-Day Delivery", "in Selected Cities"));

        return section;
    }

    private JPanel createStatCard(String emoji, String value, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BACKGROUND_COLOR);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(new Color(108, 117, 125));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        card.add(labelText);

        return card;
    }

    private JPanel createMedicineCard(Medicine medicine) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Medicine Image Placeholder
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(150, 150));
        imagePanel.setMaximumSize(new Dimension(150, 150));
        imagePanel.setBackground(new Color(248, 249, 250));
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        JLabel imagePlaceholder = new JLabel("💊");
        imagePlaceholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(imagePlaceholder, BorderLayout.CENTER);

        // Medicine Name
        JLabel nameLabel = new JLabel("<html>" + medicine.getName().toUpperCase() + "</html>");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Company Name
        JLabel companyLabel = new JLabel("By " + medicine.getCategory());
        companyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        companyLabel.setForeground(new Color(108, 117, 125));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Price Section
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pricePanel.setBackground(CARD_COLOR);
        pricePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        double originalPrice = medicine.getPrice() * 1.5;
        JLabel mrpLabel = new JLabel("<html><strike>MRP ₹" + String.format("%.0f", originalPrice) + "</strike></html>");
        mrpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        mrpLabel.setForeground(new Color(108, 117, 125));

        JLabel priceLabel = new JLabel("₹ " + String.format("%.0f", medicine.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(TEXT_PRIMARY);

        double discount = ((originalPrice - medicine.getPrice()) / originalPrice) * 100;
        JLabel discountLabel = new JLabel(String.format("%.0f%% OFF", discount));
        discountLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        discountLabel.setForeground(SUCCESS_COLOR);

        pricePanel.add(priceLabel);
        pricePanel.add(discountLabel);

        // Add to Cart Button
        JButton addButton = new JButton("Add to Cart");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(200, 40));
        
        addButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                medicine.getName() + " added to cart!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Add hover effect
        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(PRIMARY_COLOR);
            }
        });

        card.add(imagePanel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(companyLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(mrpLabel);
        card.add(pricePanel);
        card.add(Box.createVerticalStrut(10));
        card.add(addButton);

        return card;
    }

    private JButton createIconButton(String emoji, String text) {
        JButton button = new JButton("<html><center>" + emoji + "<br/>" + text + "</center></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(TEXT_PRIMARY);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        return button;
    }

    private void loadMedicines() {
        List<Medicine> medicines = medicineService.getAllMedicines();
        medicineCardsPanel.removeAll();
        
        // Limit to 10 medicines for the grid
        int count = Math.min(medicines.size(), 10);
        for (int i = 0; i < count; i++) {
            medicineCardsPanel.add(createMedicineCard(medicines.get(i)));
        }
        
        medicineCardsPanel.revalidate();
        medicineCardsPanel.repaint();
    }

    private void searchMedicines() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadMedicines();
            return;
        }
        
        List<Medicine> medicines = medicineService.searchMedicines(query);
        medicineCardsPanel.removeAll();
        
        for (Medicine medicine : medicines) {
            medicineCardsPanel.add(createMedicineCard(medicine));
        }
        
        medicineCardsPanel.revalidate();
        medicineCardsPanel.repaint();
    }

    private void setupButtonActions() {
        viewCartButton.addActionListener((ActionEvent e) -> {
            dispose();
            new Cart(new com.medstore.models.User(userId, "", "", "", "user")).setVisible(true);
        });

        viewOrdersButton.addActionListener((ActionEvent e) -> {
            dispose();
            new OrderHistory(userId).setVisible(true);
        });

        logoutButton.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            UserDashboard dashboard = new UserDashboard(1);
            dashboard.setVisible(true);
        });
    }
}