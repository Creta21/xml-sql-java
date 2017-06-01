package customSqlQuery;

import java.time.LocalDate;

public class ConverterDates {
    
    public java.sql.Date convertToDatabaseDate(LocalDate entityDate) {
        return java.sql.Date.valueOf(entityDate);
    }

    public LocalDate convertToLocalDate(java.sql.Date databaseDate) {
        return databaseDate.toLocalDate();
    }
}
