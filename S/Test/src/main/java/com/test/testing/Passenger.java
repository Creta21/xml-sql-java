package com.test.testing;

public abstract class Passenger {

	protected String firstName;

	protected String lastName;


	public String toString() {

		return firstName + " " + lastName ;

	}

	public abstract boolean isAuthorizedTrain(String code);
}
