package thinking_12_22;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class SchemaValidator {

    public static boolean validateSchema(String xsd, String xml){

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = schemaFactory.newSchema(new File(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (SAXException | IOException e) {
            System.out.println("Error:" + e.getMessage());
            return false;
        }

        return true;
    }

}
