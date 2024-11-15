import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ApartmentManagementDB";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "12345"; // Your MySQL password

    public static Connection getConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return the connection object
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return null;
        }
    }
}