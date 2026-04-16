package perfumeshop.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    protected Connection connection;

    public DBContext() {
        this.connection = getConnection();
    }

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://Lenovo-ThinkPad-T14:1433;databaseName=Perfume_Website;encrypt=true;trustServerCertificate=true";
            String username = "sa";
            String password = "12345";

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("DB connection failed", ex);
        }
    }
}