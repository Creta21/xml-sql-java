package com.test.testing;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/*
    Video gia validation: https://www.youtube.com/watch?v=6qt8ZwqwWvk
*/

public class Validation {

    boolean flag = true;      


    public boolean isValide() {

        try {
            validate("src/main/java/com/test/xml/TestRQ.xml", "src/main/java/com/test/xml/TestRQ.xsd");
        } catch (SAXException e) {
            flag = false;
            System.out.println("SAXException " + e);
        } catch (IOException e) {
            flag = false;
            System.out.println("IOException " + e);
        }
        
        return flag;
    }

    public static void validate(String xmlFile, String validationFile) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ((schemaFactory.newSchema(new File(validationFile))).newValidator()).validate(new StreamSource(new File(xmlFile)));
    }

}
