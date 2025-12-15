import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:dgp.db"; // local file in same folder

    static {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Initialize database (create tables if they don't exist)
            initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found in classpath", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void initDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Enforce foreign keys in SQLite
            stmt.execute("PRAGMA foreign_keys = ON");

            // Create clients table if not exists
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clients (
                    client_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    name           TEXT NOT NULL,
                    contact_person TEXT,
                    phone          TEXT,
                    email          TEXT,
                    address        TEXT,
                    notes          TEXT,
                    status         TEXT DEFAULT 'ACTIVE'
                );
            """);

            // Create projects table if not exists
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS projects (
                project_id     INTEGER PRIMARY KEY AUTOINCREMENT,
                client_id      INTEGER NOT NULL,
                project_name   TEXT,
                location       TEXT,
                date_started   TEXT,
                date_completed TEXT,
                status         TEXT,
                total_cost     REAL,
                notes          TEXT,
                specs          TEXT,
                po_number      INTEGER,
                sales_invoice  INTEGER,
                dr_number      INTEGER,
                FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
            );
        """);
        }
    }
}
