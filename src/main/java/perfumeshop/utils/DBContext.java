package perfumeshop.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQL Server connection settings are supplied by the local environment.
 * Set DB_URL, DB_USERNAME and DB_PASSWORD in the IDE/Tomcat run configuration.
 */
public class DBContext {

    protected Connection connection;

    public DBContext() {
        this.connection = getConnection();
    }

    private static String requiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing environment variable " + name
                    + ". See .env.example and README.md."
            );
        }
        return value;
    }

    public static Connection getConnection() {
        String url = requiredEnv("DB_URL");
        String username = requiredEnv("DB_USERNAME");
        String password = requiredEnv("DB_PASSWORD");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new IllegalStateException("SQL Server connection failed", ex);
        }
    }
}
