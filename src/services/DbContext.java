package services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbContext {
    public Connection connect(String dbUrl, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
