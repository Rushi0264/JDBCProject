package org.example;

import java.sql.*;
import java.util.Scanner;

public class StudentJDBC {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/RushiDB";
        String user = "postgres";
        String password = "Rushi12@";

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully.");

            String createTableSQL = "CREATE TABLE IF NOT EXISTS student (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT, " +
                    "department VARCHAR(50)" +
                    ")";
            Statement createStmt = conn.createStatement();
            createStmt.executeUpdate(createTableSQL);

            while (true) {
                System.out.println("\n=== Student Management Menu ===");
                System.out.println("1. Insert Student");
                System.out.println("2. Display Students");
                System.out.println("3. Delete Student by ID");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                if (choice == 1) {
                    sc.nextLine();
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();
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
                    String selectQuery = "SELECT * FROM student ORDER BY id";
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
                    System.out.print("Enter ID to delete: ");
                    int id = sc.nextInt();

                    String deleteQuery = "DELETE FROM student WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(deleteQuery);
                    ps.setInt(1, id);
                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Record deleted successfully.");

                        // Reorder IDs and reset sequence (not recommended in production)
                        Statement reorderStmt = conn.createStatement();
                        reorderStmt.executeUpdate(
                                "WITH ordered AS (" +
                                        "SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS new_id FROM student" +
                                        ") " +
                                        "UPDATE student SET id = ordered.new_id " +
                                        "FROM ordered WHERE student.id = ordered.id;"
                        );
                        reorderStmt.executeUpdate(
                                "SELECT setval('student_id_seq', (SELECT COALESCE(MAX(id), 0) FROM student));"
                        );
                        System.out.println("IDs reordered successfully.");
                    } else {
                        System.out.println("No record found with that ID.");
                    }

                } else if (choice == 4) {
                    System.out.println("Exiting program...");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }

            conn.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
