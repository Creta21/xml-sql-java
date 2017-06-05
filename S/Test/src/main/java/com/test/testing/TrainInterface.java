package com.test.testing;

import com.test.database.OriginDestination;
import java.util.List;

public interface TrainInterface {

	List<TrainTraveler> getPassengers();

	String getDeparture();

	String getArrival();

	List<OriginDestination> getTrains(String departureCode, String arrivalCode);

	void printTrain();

}
