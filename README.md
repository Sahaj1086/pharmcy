# Medicine Management System

## Overview
The Medicine Management System is a desktop application designed to manage medicines in a pharmacy-like environment. It features a user-friendly GUI built with Java Swing and integrates with a MySQL database for data storage and retrieval. The application allows users to register, log in, browse medicines, manage their cart, place orders, and view order history. Admin users can manage the inventory, including adding, updating, and removing medicines.

## Features
- User login and registration
- Admin dashboard for inventory management
- Search and filter options for medicines
- Cart management with add/remove functionality
- Order placement with PDF bill generation
- Order history tracking
- Automatic alerts for low stock and expired medicines

## Project Structure
```
medstore-app
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── medstore
│       │           ├── DBConnection.java
│       │           ├── Main.java
│       │           ├── models
│       │           │   ├── User.java
│       │           │   ├── Medicine.java
│       │           │   ├── CartItem.java
│       │           │   ├── Order.java
│       │           │   └── OrderItem.java
│       │           ├── ui
│       │           │   ├── Login.java
│       │           │   ├── Register.java
│       │           │   ├── UserDashboard.java
│       │           │   ├── MedicineDetails.java
│       │           │   ├── Cart.java
│       │           │   ├── OrderHistory.java
│       │           │   └── AdminDashboard.java
│       │           ├── services
│       │           │   ├── UserService.java
│       │           │   ├── MedicineService.java
│       │           │   ├── CartService.java
│       │           │   ├── OrderService.java
│       │           │   └── BillGenerator.java
│       │           └── utils
│       │               └── Utils.java
│       └── resources
│           └── db.properties
├── db
│   └── setup.sql
├── lib
│   └── itextpdf-x.x.x.jar
├── pom.xml
└── README.md
```

## Database Setup
To set up the database, execute the SQL script located in the `db/setup.sql` file. This script will create the necessary tables for users, medicines, cart, orders, and order items.

## Running the Application
1. Ensure you have Java Development Kit (JDK) and MySQL installed on your machine.
2. Clone the repository or download the project files.
3. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
4. Configure the database connection in `src/main/resources/db.properties` with your MySQL credentials.
5. Build the project using Maven (if applicable).
6. Run the `Main.java` file to start the application.

## Dependencies
- Java Swing for GUI
- MySQL Connector for database connectivity
- iText PDF library for generating bills

## License
This project is licensed under the MIT License. Feel free to modify and use it as per your requirements.