package org.bootcamp.myflight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserParserHandler extends DefaultHandler {

    private ArrayList<AirlineOffer> airlineOfferList = new ArrayList<>();
    private Stack elementStack = new Stack();
    private Stack objectStack = new Stack();

    private HashMap<String, String> travelers = new HashMap<String, String>();
    private HashMap<String, FlightSegment> flightSegments = new HashMap<String, FlightSegment>();
    private HashMap<String, Flight> flights = new HashMap<String, Flight>();
    private HashMap<String, OriginDestination> originDestinations = new HashMap<String, OriginDestination>();
    private HashMap<String, PriceClass> priceClasses = new HashMap<String, PriceClass>();
    private HashMap<String, Service> services = new HashMap<String, Service>();

    public void startDocument() throws SAXException {
        //System.out.println("start of the document   : ");
    }

    public void endDocument() throws SAXException {
        //System.out.println("end of the document document     : ");

        for (AirlineOffer ao : airlineOfferList) {
            ao.insertData(originDestinations, flights, travelers, services, priceClasses);
        }

        for(Flight f:flights.values())
        {
            f.insertFlightSegments(flightSegments);
        }

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //Push it in element stack
        this.elementStack.push(qName);
        //If this is start of 'AirlineOffer' element then prepare a new AirlineOffer instance and push it in object stack
        if ("AirlineOffer".equals(qName)) {
            AirlineOffer airoffer = new AirlineOffer();
            this.objectStack.push(airoffer);

        } else if ("FlightSegmentReference".equals(qName)) //4
        {
            FlightSegmentReference flightSegmentReference = new FlightSegmentReference();

            if (attributes != null && attributes.getLength() == 1) {
                flightSegmentReference.setSegmentName(attributes.getValue(0));
            }
            this.objectStack.push(flightSegmentReference);
        } else if ("OfferPrice".equals(qName)) //6
        {
            OfferPrice offerPrice = new OfferPrice();
            this.objectStack.push(offerPrice);

        } else if ("AnonymousTraveler".equals(qName)) // Hash Map travelers
        {
            if (attributes != null && attributes.getLength() == 1) {
                this.objectStack.push(attributes.getValue(0));
            }
        } else if ("FlightSegment".equals(qName)) // FlightSegment start
        {
            FlightSegment flightSegment = new FlightSegment();

            if (attributes != null && attributes.getLength() == 1) {
                flightSegment.setName(attributes.getValue(0));
            }
            this.objectStack.push(flightSegment);
        } else if ("Flight".equals(qName)) // Flight start
        {

            if (attributes != null && attributes.getLength() == 1) {
                Flight flight = new Flight();
                flight.setName(attributes.getValue(0));
                this.objectStack.push(flight);
            }
        } else if ("OriginDestination".equals(qName)) // OriginDestination start
        {
            OriginDestination originDestination = new OriginDestination();

            if (attributes != null && attributes.getLength() == 1) {
                originDestination.setName(attributes.getValue(0));
            }
            this.objectStack.push(originDestination);
        } else if ("PriceClass".equals(qName)) // PriceClass start
        {
            String current = (String) elementStack.pop();

            if ("PriceClassList".equals(elementStack.peek())) {
                PriceClass priceClass = new PriceClass();
                if (attributes != null && attributes.getLength() == 1) {
                    priceClass.setName(attributes.getValue(0));
                }
                this.objectStack.push(priceClass);
            }
            elementStack.push(current);
        } else if ("Service".equals(qName)) // Service start
        {
            Service service = new Service();

            if (attributes != null && attributes.getLength() == 1) {
                service.setKey(attributes.getValue(0));
            }
            this.objectStack.push(service);
        }

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Remove last added  element
        this.elementStack.pop();
        //AirlineOffer instance has been constructed so pop it from object stack and push in userList
        if ("AirlineOffer".equals(qName)) {
            AirlineOffer object = (AirlineOffer) this.objectStack.pop();
            this.airlineOfferList.add(object);
            
        } else if ("FlightSegmentReference".equals(qName)) //4 end
        {
            FlightSegmentReference flightSegmentReference = (FlightSegmentReference) this.objectStack.pop();

            AirlineOffer myairlineOffer = (AirlineOffer) this.objectStack.peek();
            myairlineOffer.addFSReference(flightSegmentReference);
        } else if ("OfferPrice".equals(qName)) // 6 end
        {
            OfferPrice offerPrice = (OfferPrice) this.objectStack.pop();

            AirlineOffer myairlineOffer = (AirlineOffer) this.objectStack.peek();
            myairlineOffer.addOfferPrice(offerPrice);
        } else if ("FlightSegment".equals(qName)) //FlightSegment end
        { 
            
            FlightSegment flightSegment = (FlightSegment) this.objectStack.pop();
            flightSegments.put(flightSegment.getName(), flightSegment);
        } else if ("Flight".equals(qName) && "FlightList".equals(currentElement())) //Flight end
        {
            Flight flight = (Flight) this.objectStack.pop();
            flights.put(flight.getName(), flight);
            // System.out.println("****************************************************************** \n " + flights);
        } else if ("OriginDestination".equals(qName)) //OriginDestination end
        {
            OriginDestination originDestination = (OriginDestination) this.objectStack.pop();
            originDestinations.put(originDestination.getName(), originDestination);
        } else if ("PriceClass".equals(qName)) //PriceClass end
        {

            if ("PriceClassList".equals(elementStack.peek())) {
                PriceClass priceClass = (PriceClass) this.objectStack.pop();
                priceClasses.put(priceClass.getName(), priceClass);
            }
        } else if ("Service".equals(qName)) //Service end
        {
            Service service = (Service) this.objectStack.pop();
            services.put(service.getKey(), service);
        }

    }

    /**
     * This will be called everytime parser encounter a value node
     *
     */

    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (value.length() == 0) {
            return; // ignore white space
        }

        //handle the value based on to which element it belongs
        if ("Total".equals(currentElement())) //1
        {
            String current = (String) elementStack.pop();
            if ("DetailCurrencyPrice".equals(elementStack.peek())) {
                AirlineOffer airoffer = (AirlineOffer) this.objectStack.peek();
                BigDecimal bd = new BigDecimal(value);
                airoffer.setTotalPrice(bd);

            }/*~~~~~~~~~~~~~~~ S e r v i c e ~~~~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("Price".equals(elementStack.peek())) {
                this.objectStack.push(value);
            }
            elementStack.push(current);

        } else if ("PriceClassReference".equals(currentElement())) //2
        {
            String current = (String) elementStack.pop();

            AirlineOffer airoffer = (AirlineOffer) this.objectStack.peek();
            airoffer.setPriceClassReference(value);

            elementStack.push(current);
        } else if ("FlightReferences".equals(currentElement())) //3
        {
            String current = (String) elementStack.pop();
            if ("ApplicableFlight".equals(elementStack.peek())) {
                AirlineOffer airoffer = (AirlineOffer) this.objectStack.peek();
                airoffer.setFlightReference(value);
            }/*~~~~~~ O r i g i n   D e s t i n a t i o n  ~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("OriginDestination".equals(currentElement())) {
                OriginDestination originDestination = (OriginDestination) this.objectStack.peek();
                originDestination.setFlightReferences(value);
            }
            elementStack.push(current);

        } else if ("CabinDesignator".equals(currentElement())) //5
        {

            FlightSegmentReference flightSegmentReference = (FlightSegmentReference) this.objectStack.peek();
            flightSegmentReference.setCabin(value);

        } else if ("SimpleCurrencyPrice".equals(currentElement())) //6
        {
            //String current = (String) elementStack.pop();

            OfferPrice offerPrice = (OfferPrice) this.objectStack.peek();
            BigDecimal bd = new BigDecimal(value);
            offerPrice.setTotalAmount(bd);

            //elementStack.push(current);
        } else if ("TravelerReferences".equals(currentElement())) //7
        {
            String current = (String) elementStack.pop();

            if ("AssociatedTraveler".equals(elementStack.peek())) {
                OfferPrice offerPrice = (OfferPrice) this.objectStack.peek();
                offerPrice.setTraveler(value);
            }
            elementStack.push(current);
        } else if ("PTC".equals(currentElement())) // Hash Map travelers
        {
            String objectKey = (String) objectStack.pop();

            travelers.put(objectKey, value);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ A I R P O R T  C O D E ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("AirportCode".equals(currentElement())) // Hash Map FlightSegment
        {
            String current = (String) elementStack.pop();

            if ("Departure".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setDepartureAirportCode(value);
            } else if ("Arrival".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setArrivalAirportCode(value);
            }
            elementStack.push(current);

        } /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ D A T E ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Date".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("Departure".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();

                LocalDate date = LocalDate.parse(value);
                flightSegment.setDepartureDate(date);

            } else if ("Arrival".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();

                LocalDate date = LocalDate.parse(value);
                flightSegment.setArrivalDate(date);
            }
            elementStack.push(current);

        } /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ T I M E ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Time".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("Departure".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();

                LocalTime time = LocalTime.parse(value);
                flightSegment.setDepartureTime(time);

            } else if ("Arrival".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();

                LocalTime time = LocalTime.parse(value);
                flightSegment.setArrivalTime(time);

            }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ T I M E  D U R A T I O N ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("Journey".equals(elementStack.peek())) {
                Flight flight = (Flight) this.objectStack.peek();
                flight.setDuration(value);
            }

            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ A I R P O R T  N A M E ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("AirportName".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("Departure".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setDepartureAirportName(value);
            } else if ("Arrival".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setArrivalAirportName(value);
            }
            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ A I R L I N E  I D ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("AirlineID".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("OperatingCarrier".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setAirline(value);
            }

            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ F L I G H T  N U M B E R ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("FlightNumber".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("OperatingCarrier".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setFlightNumber(value);
            }

            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ A I R C R A F T ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Name".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("Equipment".equals(elementStack.peek())) {
                FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
                flightSegment.setAircraft(value);
            }/*~~~~~~~~~~~~~~~ P r i c e   C l a s s ~~~~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("PriceClass".equals(elementStack.peek())) {
                PriceClass priceClass = (PriceClass) this.objectStack.peek();
                priceClass.setNamePrice(value);
            }/*~~~~~~~~~~~~~~~ S e r v i c e ~~~~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("Service".equals(elementStack.peek())) {
                Service service = (Service) this.objectStack.peek();
                service.setName(value);
            }

            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ F L I G H T  D U R A T I O N ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Value".equals(currentElement())) {
            FlightSegment flightSegment = (FlightSegment) this.objectStack.peek();
            flightSegment.setFlightDuration(value);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~ S e g m e n t   R e f e r e n c e s  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("SegmentReferences".equals(currentElement()) && "FlightList".equals(getCurrentElementGrandParent())) {
            
            Flight flight = (Flight) this.objectStack.peek();
            flight.setSegments(value);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~ O r i g i n   D e s t i n a t i o n  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("DepartureCode".equals(currentElement())) {
            OriginDestination originDestination = (OriginDestination) this.objectStack.peek();
            originDestination.setDepartureCode(value);
        } else if ("ArrivalCode".equals(currentElement())) {
            OriginDestination originDestination = (OriginDestination) this.objectStack.peek();
            originDestination.setArrivalCode(value);
        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~ P r i c e   C l a s s  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Code".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("PriceClass".equals(elementStack.peek())) {
                PriceClass priceClass = (PriceClass) this.objectStack.peek();
                priceClass.setCodePrice(value);
            }/*~~~~~~~~~~~~~~~ P r i c e   C l a s s ~~~~~~~~~~~~~~~~~~~~~~~*/ 
            else if ("FareBasisCode".equals(elementStack.peek())) {
                PriceClass priceClass = (PriceClass) this.objectStack.peek();
                priceClass.setFareBasisCode(value);
            }

            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~ P r i c e   C l a s s  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("ServiceReferences".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("AssociatedService".equals(elementStack.peek())) {
                OfferPrice offerPrice = (OfferPrice) this.objectStack.peek();
                offerPrice.setServicesReferences(value);
            }
            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~~~~~ S e r v i c e  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("Text".equals(currentElement())) {
            Service service = (Service) this.objectStack.peek();
            service.setText(value);
        } else if ("PassengerReferences".equals(currentElement())) {
            String current = (String) elementStack.pop();

            if ("Price".equals(elementStack.peek())) {
                String myStringTotal = (String) objectStack.pop();
                Service service = (Service) this.objectStack.peek();

                int myIntTotal = Integer.parseInt(myStringTotal);
                service.setPrices(value, myIntTotal);
            }
            elementStack.push(current);

        }/*~~~~~~~~~~~~~~~~~~~~~~ Origin Destination References  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
        else if ("OriginDestinationReferences".equals(currentElement())) {
            AirlineOffer originDestinationReference = (AirlineOffer) this.objectStack.peek();
            originDestinationReference.setOriginDestinationReference(value);
        }

    }

    /**
     * Utility method for getting the current element in processing
     *
     */
    private String currentElement() {
        return (String) this.elementStack.peek();
    }

    //Accessor for userList object
    public ArrayList getAirlineOffers() {
        return airlineOfferList;
    }

    public HashMap<String, String> getTravelers() {
        return this.travelers;
    }
    
    public String getCurrentElementGrandParent()
    {
        String currentElement = (String) elementStack.pop();
        String currentElementParent = (String) elementStack.pop();
        String currentElementGrandParent = (String) elementStack.peek();
        
        elementStack.push(currentElementParent);
        elementStack.push(currentElement);
        
        return currentElementGrandParent;
        
    }

    public HashMap<String, OriginDestination> getOriginDestination() {
        return originDestinations;
    }
    
    
}
