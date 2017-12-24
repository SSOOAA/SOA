package assignment4;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XSLTansform {

    public static void main(String[] args) {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/assignment4/ScoreList.xsl"));
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer(xslt);

        } catch (TransformerConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Source input = new StreamSource(new File("src/assignment4/StudentList.xml"));
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(input, new StreamResult(new File("src/assignment4/XML文档3.xml")));
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
