import java.sql.*;

public class HiveOperations {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://localhost:10000/default";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Hive JDBC Driver not found in classpath");
            System.err.println("Make sure hive-jdbc-4.0.1.jar is properly included");
            System.exit(1);
        }

        try {
            System.out.println("Connecting to Hive server...");
            conn = DriverManager.getConnection(url, "", "");
            stmt = conn.createStatement();

            // Create tables
            System.out.println("Creating tables...");
            stmt.execute("DROP TABLE IF EXISTS employees");
            stmt.execute("CREATE TABLE employees (" +
                        "emp_id INT, " +
                        "first_name STRING, " +
                        "last_name STRING, " +
                        "salary DOUBLE, " +
                        "dept_id INT) " +
                        "ROW FORMAT DELIMITED FIELDS TERMINATED BY ','");

            // Alter table operations
            System.out.println("Altering tables...");
            stmt.execute("ALTER TABLE employees ADD COLUMNS (hire_date STRING)");
            stmt.execute("ALTER TABLE employees CHANGE salary salary DECIMAL(10,2)");
            stmt.execute("ALTER TABLE employees SET TBLPROPERTIES ('comment' = 'Employee records')");

            // Show table description
            System.out.println("Table description:");
            ResultSet rs = stmt.executeQuery("DESCRIBE employees");
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2));
            }

            // Drop table
            System.out.println("Dropping table...");
            stmt.execute("DROP TABLE IF EXISTS employees PURGE");

        } catch (SQLException e) {
            System.err.println("Error connecting to Hive server: " + e.getMessage());
            System.err.println("Make sure Hive server is running on port 10000");
            System.exit(1);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }
}