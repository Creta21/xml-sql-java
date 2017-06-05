package com.test.testing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

public class ConnectionToDatabase {
    //2os tropos gia na kanoume connect me ti DB mas
    public Connection connect() throws Exception{
        try {
            String driver = "com.mysql.jdbc.Driver";            
            Class.forName(driver);
            ArrayList<String> properties = this.getProperties();
            Connection conn = DriverManager.getConnection(properties.get(0), properties.get(1), properties.get(2));
//            System.out.println("DataBase Connected");
            return conn;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
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