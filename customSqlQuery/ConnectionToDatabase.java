package customSqlQuery;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionToDatabase {
    public Connection connect() throws Exception{
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/schemaName";
            String username = "root";
            String password = "root";
            
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url,username,password);
//            System.out.println("DataBase Connected");
            return conn;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
