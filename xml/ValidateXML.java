package xml;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class ValidateXML {
    public Boolean isvalid() throws Exception{
        boolean flag = true;
        try{
            validate("src/main/java/com/xml/Train.xml","src/main/java/com/xml/Train.xsd");
        } catch (SAXException e) {
            flag = false;
            System.out.println("SAXException: " + e);
        } catch (IOException e) {
            flag = false;
            System.out.println("IOException: " + e);
        }
      return flag;  
    }
    // Method for validation XML file.
    public static void validate(String xmlFile, String validationFile) throws SAXException, IOException{
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ((schemaFactory.newSchema(new File(validationFile))).newValidator()).validate(new StreamSource(new File(xmlFile))); 
    }
}
