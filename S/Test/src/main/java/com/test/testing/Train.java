
package com.test.testing;

import java.util.List;


public class Train {
    
    private String departure;
    private String arrival;
    private List<TrainTraveler> TrainTravelers;

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public List<TrainTraveler> getTrainTravelers() {
        return TrainTravelers;
    }

    public void setTrainTravelers(List<TrainTraveler> TrainTravelers) {
        this.TrainTravelers = TrainTravelers;
    }

}
