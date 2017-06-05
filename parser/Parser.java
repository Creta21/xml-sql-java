package parser;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Parser {
    public Object parserXml(InputStream in){
        //Dhmiourgia object typou Handler
        //Create default handler instance
        Handler handler = new Handler();
        
        try{
            //Dhmiourgia parser apo class XMLReader
            //Create parser from factory
            XMLReader parser = XMLReaderFactory.createXMLReader();
            
            //parser handler
            //Register handler with parser
            parser.setContentHandler(handler);
            
            //Create an input source from the XML input stream
            InputSource source = new InputSource(in);
            
            //parse the document
            parser.parse(source);
           
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
        }
        return handler.getData();
    }
}
