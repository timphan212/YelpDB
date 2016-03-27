package yelpdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Timothy
 */
public class populate {
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error loading driver: " + cnfe);
        }
        
        String url = "jdbc:oracle:thin:@localhost:1521:orclg";
        Connection conn = DriverManager.getConnection(url, "scott", "tiger");
        DatabaseMetaData md = conn.getMetaData();
        System.out.println("db:" + md.getDatabaseProductName());
    }
    
}
