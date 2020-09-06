package vitaly.mosin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String JDBC = "jdbc:h2:";
    private static final String HOST_AND_PORT = "//localhost:3306";


    private static Connection connection = null;

    public static Connection getConnect(String dbName) {

        String fullUrl = JDBC + "./target/classes/dev/" + dbName;
//        String fullUrl = JDBC + HOST_AND_PORT+dbName;
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(fullUrl, USER, PASSWORD);
            }else if (connection.isClosed()){
                connection = DriverManager.getConnection(fullUrl, USER, PASSWORD);
            } else {
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(String dbName) {
        try {
         getConnect(dbName).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
