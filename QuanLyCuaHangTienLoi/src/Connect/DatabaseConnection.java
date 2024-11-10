package Connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLCHTL";
        String user = "sa";
        String password = "123123";
        return DriverManager.getConnection(url, user, password);
    }
}