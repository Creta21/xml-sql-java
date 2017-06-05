package com.test.testing;

import java.io.FileNotFoundException;


public class TestRQMain {
    
    public static void main(String[] args) throws FileNotFoundException{
        
        Validation valid = new Validation();
        if (valid.isValide()) {
            
            TrainSystem securityTrainSystem = new TrainSystem();
            
            securityTrainSystem.printTrain();
            
        } else {
            System.out.println("XML invalid");
        }

    }   
    
}
