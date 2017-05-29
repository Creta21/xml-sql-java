package customSqlQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QueriesSelect { 
    
    public List<Trip> getTrips(String departureStationCode, String arrivalStationCode) {
        ArrayList<Trip> trips = new ArrayList<>();
        try {
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement statement = conector.connect().prepareStatement("SELECT * FROM trip WHERE departure_station = '" + departureStationCode + "' AND arrival_station = '" + arrivalStationCode + "'");
            ResultSet result = statement.executeQuery();
           
            while(result.next()) {
                Trip trip = new Trip();
                
                trip.setDeparture_station(result.getString("departure_station"));
                trip.setArrival_station(result.getString("arrival_station"));
                trip.setTrip_number(result.getString("trip_number"));
                trip.setStop_station(result.getString("stop_station"));
                trips.add(trip);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return trips;
    }
    
    public List<Person> getPersons() {
        ArrayList<Person> persons = new ArrayList<>();
        try {
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement statement = conector.connect().prepareStatement("SELECT * FROM person");
            ResultSet result = statement.executeQuery();
           
            while(result.next()) {
                Person person = new Person();
                
                person.setId(result.getInt("id"));
                person.setPassport_number(result.getString("passport_number"));
                person.setLastname(result.getString("lastname"));
                person.setFirstname(result.getString("firstname"));
                person.setGender(result.getString("gender"));
                persons.add(person);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return persons;
    }
}
