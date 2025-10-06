package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_management";
    private static final String USER = "root";     
    private static final String PASS = "Root";  

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("DB Connection Error: " + e.getMessage());
            return null;
        }
    }
}
