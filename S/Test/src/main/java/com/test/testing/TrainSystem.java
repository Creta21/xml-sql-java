
package com.test.testing;

import com.test.database.NoTrain;
import com.test.database.OriginDestination;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class TrainSystem implements TrainInterface{
    
    private Train train = new Train();
    private List<OriginDestination> ods = new ArrayList<>();
    private TrainTraveler trainTraveler = new TrainTraveler();

    public TrainSystem() throws FileNotFoundException{
        
        File xmlFile = new File("src/main/java/com/test/xml/TestRQ.xml");
        
        //Create the parser instance
        XmlParser parser = new XmlParser();

        //Parse the file
        train = parser.parseXml(new FileInputStream(xmlFile));

    } 
    
    @Override
    public List<TrainTraveler> getPassengers() {        
        return train.getTrainTravelers();            
    }

    @Override
    public String getDeparture() {
        return train.getDeparture();
    }

    @Override
    public String getArrival() {
        return train.getArrival();
    }

    
    @Override 
    public List<OriginDestination> getTrains(String departureCode, String arrivalCode) {
        ArrayList<OriginDestination> oDflights = new ArrayList<>();
        try {
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement statement = conector.connect().prepareStatement("SELECT * FROM train WHERE departure_train = '"+departureCode+"' AND arrival_train = '"+arrivalCode+"'");
            ResultSet result = statement.executeQuery();
           
            while(result.next()) {
                OriginDestination od = new OriginDestination();
                
                od.setDeparture(result.getString("departure_train"));
                od.setArrival(result.getString("arrival_train"));
                od.setTrainNumber(result.getString("train_number"));
                od.setStopTrain(result.getString("stop_train"));
                oDflights.add(od);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return oDflights;
    }
    
    public List<NoTrain> getNoTrain() {
        ArrayList<NoTrain> noFlies = new ArrayList<>();
        try {
            ConnectionToDatabase conector = new ConnectionToDatabase();
            PreparedStatement statement = conector.connect().prepareStatement("SELECT * FROM notrain");
            ResultSet result = statement.executeQuery();
           
            while(result.next()) {
                NoTrain noFlight = new NoTrain();
                
                noFlight.setForbiddenTrain(result.getString("forbidden_train"));
                noFlight.setId(result.getString("id"));
                noFlight.setPersonId(result.getString("person_id"));
                noFlies.add(noFlight);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return noFlies;
    }

    @Override
    public void printTrain() {        
        
        ods = getTrains(train.getDeparture(), train.getArrival());
        int i = 0;
        
        System.out.println("From: " + train.getDeparture());
        System.out.println("To: " + train.getArrival());
        System.out.println("Passenger number: " + train.getTrainTravelers().size());
        System.out.println("Train number: " + ods.size());
        
        for (OriginDestination od: ods) {
            System.out.print("Train: " + od.getTrainNumber() + ": " + od.getDeparture());
            if(od.getStopTrain() != null) {
                System.out.print(" - " + od.getStopTrain());
            }
            System.out.println(" - " + od.getArrival());
            i = 0;
            List<TrainTraveler> recognizedTrainTravelers = train.getTrainTravelers();
            for(TrainTraveler rt: recognizedTrainTravelers) {
                i++;
                System.out.println("\tPassenger" + i + ": " + rt.getLastName() + " " + rt.getFirstName() + " > " + isCleared(rt, od));
            }
            System.out.println("");
            
        }
        writeFile();
    }
    
    public void writeFile() { // gia na gurizoyn ta apotelesmata se ena arxeio .txt pou tha apothikeuetai ekei
        System.out.println("Write Train File"); // pou leei to url parakatw
        
        try (PrintWriter out = new PrintWriter("C:/Users/UserName/Desktop/clearance.txt")) {
            int i = 0;
        
            out.println("From: " + train.getDeparture());
            out.println("To: " + train.getArrival());
            out.println("Passenger number: " + train.getTrainTravelers().size());
            out.println("Train number: " + ods.size());

            for (OriginDestination od: ods) {
                out.print("Train: " + od.getTrainNumber() + ": " + od.getDeparture());
                if(od.getStopTrain() != null) {
                    out.print(" - " + od.getStopTrain());
                }
                out.println(" - " + od.getArrival());
                i = 0;
                List<TrainTraveler> recognizedTravelers = train.getTrainTravelers();
                for(TrainTraveler rt: recognizedTravelers) {
                    i++;
                    out.println("\tPassenger" + i + ": " + rt.getLastName() + " " + rt.getFirstName() + " > " + isCleared(rt, od));
                }
                out.println("");
            }
        }catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
        
        System.out.println("File Train txt is ready");
    }
    
    public String isCleared(TrainTraveler rt, OriginDestination od ) {
        
        rt.addNoFlies(getNoTrain());
      
        if(rt.isAuthorizedTrain(od.getDeparture()) || rt.isAuthorizedTrain(od.getArrival())) {
            return "NO";
        } else if (od.getStopTrain() != null) {
            if (rt.isAuthorizedTrain(od.getStopTrain())) {
                return "NO";
            }
        }
        
        return "YES";
        
    }
    
}
