package com.test.testing;

import java.util.ArrayList;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Handler extends DefaultHandler {

    private ArrayList<TrainTraveler> trainTravelers = new ArrayList<>();
    private Train train = null;
    private Stack elementStack = new Stack();
    private Stack objectStack = new Stack();

//    public ArrayList getData() {
//        // return availablebooks;
//        return null;
//    }

    @Override
    public void startDocument() throws SAXException {
        //System.out.println("start of the document   : ");
    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //Push it in element stack
        this.elementStack.push(qName);

        if ("traveler".equals(qName)) {
            TrainTraveler trainTraveler = new TrainTraveler();
            this.objectStack.push(trainTraveler);
        } else if ("OriginDestination".equals(qName)) {
            this.train = new Train();
            this.objectStack.push(train);
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Remove last added  element
        this.elementStack.pop();

        if ("RecognizedTraveler".equals(qName)) {
            TrainTraveler recognizedTraveler = (TrainTraveler) this.objectStack.pop();
            this.trainTravelers.add(recognizedTraveler);
//            System.out.println("   ----->  " + trainTraveler);
        } else if ("OriginDestination".equals(qName)) {
            this.train = (Train) this.objectStack.pop();
            this.train.setTrainTravelers(trainTravelers);
//            System.out.println("************* " + train);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String value = new String(ch, start, length).trim();

        if (value.length() == 0) {
            return; // ignore white space
        }

        if ("LastName".equals(currentElement())) {
            TrainTraveler recognizedTraveler = (TrainTraveler) this.objectStack.peek();
            recognizedTraveler.setLastName(value);
//            System.out.println("1 --> " + trainTraveler.getSurname());
        } else if ("FirstName".equals(currentElement())) {
            TrainTraveler trainTraveler = (TrainTraveler) this.objectStack.peek();
            trainTraveler.setFirstName(value);
//            System.out.println("2 --> " + trainTraveler.getGivenName());
        } else if ("TrainStationCode".equals(currentElement())) {
            String current = (String) elementStack.pop();
            if ("Departure".equals(elementStack.peek())) {
                Train train = (Train) this.objectStack.peek();
                train.setDeparture(value);
//                System.out.println("5 --> " + train.getDeparture());
            } else if ("Arrival".equals(elementStack.peek())) {
                Train train = (Train) this.objectStack.peek();
                train.setArrival(value);
//                System.out.println("6 --> " + train.getArrival());
            }
            elementStack.push(current);            
        }

    }

    private String currentElement() {
        return (String) this.elementStack.peek();
    }

    public ArrayList getTrainTraveler() {
        return this.trainTravelers;
    }
    
    public Train getTrain() {
        return this.train;
    }

}
