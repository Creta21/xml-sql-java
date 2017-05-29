package customSqlQuery;

import java.sql.PreparedStatement;

public class CreateTable {
    public void createTable() throws Exception{
        try{
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement create = conector.connect().prepareStatement("CREATE TABLE IF NOT EXIST tablename(id int NOT NULL AUTO_INCREMENT, first varchar(255), last varchar(255), PRIMARY KEY(id))");
            create.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Table Created Successfully");
    }
}
