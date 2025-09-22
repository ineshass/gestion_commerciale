package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_entreprise?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // empty password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}