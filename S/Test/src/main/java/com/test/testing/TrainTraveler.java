package com.test.testing;

import com.test.database.NoTrain;
import java.util.ArrayList;
import java.util.List;


public class TrainTraveler extends Passenger{
    
    private String profileID;
    private List<NoTrain> noTrains = new ArrayList<>();


    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }   

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<NoTrain> getNoTrains() {
        return noTrains;
    }

    public void addNoTrains(List<NoTrain> noTrains) {
        this.noTrains = noTrains;
    }
    
    

    @Override
    public String toString() {
        return "RecognizedTraveler{" + "profileID=" + profileID + " Passenger: " + super.toString() +'}';
    }

    @Override
    public boolean isAuthorizedTrain(String code) {  
        
        for(NoTrain noTrain: noTrains) {
            if(profileID.equals(noTrain.getPersonId())) {
                if(noTrain.getForbiddenTrain().equals(code)) {
                    return true;
                }            
            }
        }                 
       
        return false;
    }
    
}
