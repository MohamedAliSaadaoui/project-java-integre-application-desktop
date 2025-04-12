package com.example.rewear;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/rewear", "root", ""
            );
            System.out.println("✅ Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
