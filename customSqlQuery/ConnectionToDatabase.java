package customSqlQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

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
    
    /////////////////////////////////////////////////////////////////////////
    //           Second way with application.properties file               //
    /////////////////////////////////////////////////////////////////////////
    public Connection connect2() throws Exception{
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
			
            HashMap<String, String> properties = this.getProperties();
            Connection conn = DriverManager.getConnection(properties.get("database"),properties.get("dbuser"),properties.get("dbpassword"));
			
            System.out.println("DataBase Connected");
            return conn;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    ///// get properties from file.
    private HashMap<String, String> getProperties() throws IOException{
        Properties prop = new Properties();
		InputStream input = null;
         HashMap<String, String> properties = new HashMap<>();
		try {
				// Take the file from Other Sources
				input = new FileInputStream("src/main/resources/database.properties");
				// load a properties file
				prop.load(input);
				// add to Hash map
				properties.put("database", prop.getProperty("database"));
				properties.put("dbuser", prop.getProperty("dbuser"));
				properties.put("dbpassword", prop.getProperty("dbpassword"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
				if (input != null) {
					try {
							input.close();
					} catch (IOException e) {
							e.printStackTrace();
					}
				}
		}
			return properties;
	} 
}
