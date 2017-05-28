package file;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileOutput {
    public void writeFile() {
        System.out.println("Writing train trip file...");
        try (PrintWriter out = new PrintWriter("C:/Users/Giannis/Desktop/trip.txt")) {
        List<Passengers> passengers = this.getPassengers();
        
        out.println("---------------------------------------Train Trip File---------------------------------------------");
        out.println("From: " + this.getDepartureStation());
        out.println("To:" + this.getDepartureStation());
        for (Trip trip : trips) {
            out.print("Train " + train.getTrain_number()); 
            int i = 0;
            for (Passenger passenger : passengers) {
                out.println("\tPassenger" + i + ": " + passenger.getFirstname() + " " + passenger.getLastname() + " Id: " + passenger.getPassengerId());
                i += 1;
            }
        }
        out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("File trip.txt is Ready.!!!");
    }
}
