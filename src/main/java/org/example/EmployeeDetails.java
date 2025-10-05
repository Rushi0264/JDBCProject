package org.example;

import java.sql.*;

public class EmployeeDetails {
    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/RushiDB";
        String user = "postgres";
        String password = "Rushi12@"; //  Replace with your actual password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL successfully.");

            // Step 1: Create Employee table if it doesn't exist
            String createTable = """
                    CREATE TABLE IF NOT EXISTS employee (
                        emp_id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        department VARCHAR(50),
                        designation VARCHAR(50),
                        salary NUMERIC(10,2),
                        hire_date DATE DEFAULT CURRENT_DATE
                    );
                    """;

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTable);
                System.out.println("Employee table is ready.");
            }

            // Step 2: Insert employee records
            String insertData = """
                    INSERT INTO employee (name, department, designation, salary, hire_date)
                    VALUES
                    ('Rohan Patil', 'IT', 'Software Engineer', 55000.00, '2024-05-15'),
                    ('Sneha Kulkarni', 'HR', 'HR Manager', 60000.00, '2023-11-20'),
                    ('Amit Sharma', 'Finance', 'Accountant', 50000.00, '2022-08-10'),
                    ('Priya Deshmukh', 'Marketing', 'Digital Marketer', 48000.00, '2024-02-01'),
                    ('Rahul Jadhav', 'Sales', 'Sales Executive', 45000.00, '2023-06-25');
                    """;

            try (Statement stmt = conn.createStatement()) {
                int rows = stmt.executeUpdate(insertData);
                System.out.println(rows + " employee records inserted.");
            } catch (SQLException e) {
                // Avoid duplicate inserts
                if (e.getSQLState().equals("23505")) {
                    System.out.println("Data already exists, skipping insert.");
                } else {
                    throw e;
                }
            }

            // Step 3: Display all employee details
            System.out.println("\n👨‍💼 Employee Details:");
            String query = "SELECT * FROM employee;";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    System.out.printf("ID: %d | Name: %s | Dept: %s | Designation: %s | Salary: %.2f | Hire Date: %s%n",
                            rs.getInt("emp_id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("designation"),
                            rs.getDouble("salary"),
                            rs.getDate("hire_date"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Database connection or query failed:");
            e.printStackTrace();
        }
    }
}
