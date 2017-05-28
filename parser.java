import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Parser {
    public void parserXml(InputStream in){
        //Dhmiourgia object typou Handler
        Handler handler = new Handler();
        
        try{
            //Dhmiourgia parser apo class XMLReader
            XMLReader parser = XMLReaderFactory.createXMLReader();
            
            //parser handler
            parser.setContentHandler(handler);
            
            InputSource source = new InputSource(in);
            
            parser.parse(source);
           
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
        }
    }
}