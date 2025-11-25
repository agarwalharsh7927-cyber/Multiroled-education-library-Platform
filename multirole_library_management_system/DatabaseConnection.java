import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // === Update these ONLY if your MySQL settings are different ===
    private static final String HOST = "localhost";
    private static final String DATABASE = "library_db";  // Make sure this DB exists
    private static final String USER = "root";            // Default XAMPP/WAMP user
    private static final String PASSWORD = "";            // Empty by default

    private static final String URL =
            "jdbc:mysql://" + HOST + "/" + DATABASE +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException ignored) {}
        }

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully.");

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found! Add mysql-connector-j .jar to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("MySQL connection failed! Check DB name, username, and password.");
            e.printStackTrace();
        }

        return connection;
    }
}
