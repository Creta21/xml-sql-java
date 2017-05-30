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
            ArrayList<String> prop = this.getProperties();
            Connection conn = DriverManager.getConnection(prop.get(0), prop.get(1), prop.get(2));
            return conn;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    ///// get properties from file.
    private ArrayList<String> getProperties() throws IOException{
        Properties prop = new Properties();
	InputStream input = null;
        ArrayList<String> properties = new ArrayList<>();
	try {
            // Take the file from Other Sources
            input = new FileInputStream("src/main/resources/database.properties");
            // load a properties file
            prop.load(input);
            // add to array list
            properties.add(prop.getProperty("database"));
            properties.add(prop.getProperty("dbuser"));
            properties.add(prop.getProperty("dbpassword"));

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
