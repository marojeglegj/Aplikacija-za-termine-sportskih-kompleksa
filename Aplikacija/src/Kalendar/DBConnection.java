package Kalendar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static final String DB_PROPERTIES_FILE = "src/db_config.properties";

    public static Connection getConnection() throws SQLException {
        String url = "";
        String username = "";
        String password = "";

        try {
            Properties properties = new Properties();
            Path filePath = Paths.get(DB_PROPERTIES_FILE);
            if (Files.exists(filePath)) {
                properties.load(Files.newInputStream(filePath));

                url = properties.getProperty("db.url");
                username = properties.getProperty("db.username");
                password = properties.getProperty("db.password");
            } else {
                throw new IOException("Datoteka konfiguracije baze podataka nije pronađena.");
            }
        } catch (IOException e) {
            System.err.println("Pogreška pri učitavanju konfiguracije baze podataka: " + e.getMessage());
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, username, password);
    }
}
