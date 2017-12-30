package assignment6.servlet;

import assignment6.model.ModifyScore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class ModifyXMLDom {
    public void updateXML(ModifyScore modifyScore,String filepath){
        File XMLfile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLfile);
            doc.getDocumentElement().normalize();

            updateElementValue(doc,modifyScore);

            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }

    }

    private static void updateElementValue(Document doc,ModifyScore modifyScore) {
        NodeList employees = doc.getElementsByTagName("学生");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Element PInfo = (Element) emp.getElementsByTagName("个人基本信息").item(0);
            String sid = PInfo.getElementsByTagName("个人编号").item(0).getFirstChild().getNodeValue();

            if (sid.equals(modifyScore.getSid())){
                Element allscore = (Element) emp.getElementsByTagName("学生成绩列表").item(0);
                NodeList scores = allscore.getElementsByTagName("学生成绩");
                Element score = null;
                for(int j=0;j<scores.getLength();j++){
                    score = (Element) scores.item(j);
                    String cid = score.getElementsByTagName("课程编号").item(0).getFirstChild().getNodeValue();
                    String stype = score.getElementsByTagName("成绩性质").item(0).getFirstChild().getNodeValue();

                    if (cid.equals(modifyScore.getCid()) && stype.equals(modifyScore.getScoreType())){
                        Node s = score.getElementsByTagName("得分").item(0).getFirstChild();
                        s.setNodeValue(String.valueOf(modifyScore.getScore()));
                        return;
                    }
                }
            }

        }
    }

}
