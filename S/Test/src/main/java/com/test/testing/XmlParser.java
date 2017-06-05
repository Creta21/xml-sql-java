package com.test.testing;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlParser {
    
    public Train parseXml(InputStream in){
        Train train = new Train();
        // ton kalw mesa apo ti methodo
        try {
            //Create default handler instance
            Handler handler = new Handler();
            
            //Create parser from factory
            XMLReader parser = XMLReaderFactory.createXMLReader();
            
            //Register handler with parser
            parser.setContentHandler(handler);
            
            //Create an input source from the XML input stream
            InputSource source = new InputSource(in);
            
            //parse the document
            parser.parse(source);

            train = handler.getTrain();
            
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        } 
        
        return train;        
    }
}
