package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/RushiDB";
        String user = "postgres";
        String password = "Rushi12@";  // replace with your actual password

        // SQL statement to create 'emp' table
        String sql = "INSERT INTO emp (name, department, salary, hire_date)\n" +
                "VALUES \n" +
                "('Rohan Patil', 'IT', 55000.00, '2024-05-15'),\n" +
                "('Sneha Kulkarni', 'HR', 45000.00, '2023-11-20'),\n" +
                "('Amit Sharma', 'Finance', 60000.00, '2022-08-10'),\n" +
                "('Priya Deshmukh', 'Marketing', 50000.00, '2024-02-01'),\n" +
                "('Rahul Jadhav', 'Sales', 48000.00, '2023-06-25'),\n" +
                "('Nikita Joshi', 'IT', 65000.00, '2022-03-12'),\n" +
                "('Sagar Patil', 'Operations', 52000.00, '2024-09-05'),\n" +
                "('Meena Rane', 'HR', 47000.00, '2023-01-17'),\n" +
                "('Ankit More', 'Finance', 58000.00, '2022-12-30'),\n" +
                "('Kavita Gawande', 'IT', 70000.00, '2024-07-19');\n";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("✅ Table 'emp' created successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error creating table:");
            e.printStackTrace();
        }
    }
}
