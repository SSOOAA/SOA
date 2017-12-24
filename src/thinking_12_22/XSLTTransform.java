package thinking_12_22;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XSLTTransform {
    public static void main(String[] args) {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/thinking_12_22/test.xsl"));
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer(xslt);

        } catch (TransformerConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Source input = new StreamSource(new File("src/thinking_12_22/test.xml"));
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(input, new StreamResult(new File("src/thinking_12_22/output.xml")));
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
