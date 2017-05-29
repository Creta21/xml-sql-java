package customSqlQuery;

import java.sql.PreparedStatement;

public class insert {
    public void insert() throws Exception{
        final String fname = "Jim";
        final String lname = "Miller";
        try {
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement insert = conector.connect().prepareStatement("INSERT INTO tablename (fname, lname) VALUES ('" + fname + "', '" + lname + "')");
            insert.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Insert Completed");
    }
}
