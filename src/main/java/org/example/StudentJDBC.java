package org.example;

import java.sql.*;
import java.util.Scanner;

public class StudentJDBC {
    public static void main(String[] args) {
        // Database connection details — change as per your setup
        String url = "jdbc:postgresql://localhost:5432/RushiDB"; // your DB name
        String user = "postgres"; // your username
        String password = "Rushi12@"; // your password

        Scanner sc = new Scanner(System.in);

        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully.");

            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS student (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT, " +
                    "department VARCHAR(50)" +
                    ")";
            Statement createStmt = conn.createStatement();
            createStmt.executeUpdate(createTableSQL);

            // Menu loop
            while (true) {
                System.out.println("\n=== Student Management Menu ===");
                System.out.println("1. Insert Student");
                System.out.println("2. Display Students");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                if (choice == 1) {
                    sc.nextLine(); // consume leftover newline
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine(); // consume newline

                    System.out.print("Enter Department: ");
                    String dept = sc.nextLine();

                    String insertQuery = "INSERT INTO student(name, age, department) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setString(1, name);
                    ps.setInt(2, age);
                    ps.setString(3, dept);
                    ps.executeUpdate();

                    System.out.println("Student record inserted successfully!");

                } else if (choice == 2) {
                    String selectQuery = "SELECT * FROM student";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);

                    System.out.println("\n--- Student Records ---");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id") +
                                " | Name: " + rs.getString("name") +
                                " | Age: " + rs.getInt("age") +
                                " | Department: " + rs.getString("department"));
                    }

                } else if (choice == 3) {
                    System.out.println("Exiting program...");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }

            // Close resources
            conn.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
