package Kalendar;

import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/termini"; 
        String username = "root";  
        String password = "";  
        return DriverManager.getConnection(url, username, password);
    }
}

