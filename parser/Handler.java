package parser;

import java.util.ArrayList;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Handler extends DefaultHandler{
    private Stack objectStack = new Stack();
    private Stack<String> elementStack = new Stack<>();
  
    private Train train = null;
    private Passenger passenger = null;
    
    private ArrayList<Passenger> passengers = new ArrayList<>();
    
    public String currentElement (){
        return this.elementStack.peek();
    }
    
    public String parentElement (){
        String child = this.elementStack.pop();
        String parent = this.elementStack.peek();
        this.elementStack.push(child);
        return parent;
    }
    
    public Object getData() {
        return this.train;
    }
    
    @Override
    public void startDocument() throws SAXException {
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.elementStack.push(localName);
        
        switch(localName) {
            case "Passenger":
                this.passenger = new Passenger();
                this.objectStack.push(this.passenger);
                break;
            case "OriginDestination":
                this.train = new Train();
                this.objectStack.push(this.train);
                break;
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        
        if (value.length() == 0) {
            return;
        }
        switch (this.currentElement()) {
            case "firstname":
                this.passenger = (Passenger) this.objectStack.peek();
                this.passenger.setFirstname(value);
                break;
            case "lastname":
                this.passenger = (Passenger) this.objectStack.peek();
                this.passenger.setLastname(value);
                break;
            case "passengerId":
                this.passenger = (Passenger) this.objectStack.peek();
                this.passenger.setPassengerId(value);
                break;
            case "Gender":
                this.passenger = (Passenger) this.objectStack.peek();
                this.passenger.setGender(value);
                break;
            case "StationCode":
                if ("Departure".equals(parentElement())) {
                    this.train = (Train) this.objectStack.peek();
                    this.train.setDepartureStation(value);
                } else if ("Arrival".equals(parentElement())){
                    this.train = (Train) this.objectStack.peek();
                    this.train.setArrivalStation(value);
                }
                break;     
        }
          
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.elementStack.pop();
        
        switch (localName) {
            case "Passenger":
                this.passenger = (Passenger) this.objectStack.pop();
                this.passengers.add(passenger);
                break;
            case "OriginDestination":
                this.train = (Train) this.objectStack.pop();
                this.train.setPassengers(passengers); // At end set list of travelers in Train Class || Can write and inEndDocument.
                break;
        }
    }
    @Override
    public void endDocument() throws SAXException {  
//        for (Rental rental : rentals){
//            for (Book book : books){
//                if (rental.getBookReference() == book.getId()) {
//                    book.addNewRental(rental);
//                }
//            }
//            
//        }
    }   
}