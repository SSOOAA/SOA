package Utils;

import assignment5.model.Score;
import assignment5.sax.MyContentHandler;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.*;
import javax.xml.soap.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class QueryScore {

    public ArrayList<Score> findScore(String sid,String filepath){
        ArrayList<Score> scoreList = new ArrayList<>();

        //使用sax解析StudentList.xml获取成绩信息
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new MyContentHandler(sid, scoreList));
            xmlReader.setErrorHandler(new DefaultErrorHandler());
            xmlReader.parse(filepath);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return scoreList;
    }

    public void createQueryScoreSOAP(ArrayList<Score> scoreList,String filepath){
        //创建SOAP消息
        try {
            SOAPConnectionFactory soapConnFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();
            SOAPPart part = message.getSOAPPart();

            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();

            //成绩列表为空，说明学号异常
            if (scoreList.isEmpty()) {
                SOAPFault fault = body.addFault();
                fault.setFaultCode("404");
                fault.setFaultString("学号不存在或者该学号对应的学生成绩不存在");
            } else {
                //将查询到的成绩信息放入一个dom中
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                org.w3c.dom.Document resultDOM = documentBuilder.newDocument();
                org.w3c.dom.Element root = resultDOM.createElement("学生成绩列表");

                for (Score score : scoreList) {
                    org.w3c.dom.Element scoreElement = resultDOM.createElement("学生成绩");

                    org.w3c.dom.Element pointElement = resultDOM.createElement("得分");
                    pointElement.setTextContent(String.valueOf(score.getPoint()));
                    scoreElement.appendChild(pointElement);

                    org.w3c.dom.Element courseIdElement = resultDOM.createElement("课程编号");
                    courseIdElement.setTextContent(String.valueOf(score.getCourseId()));
                    scoreElement.appendChild(courseIdElement);

                    org.w3c.dom.Element typeElement = resultDOM.createElement("成绩性质");
                    typeElement.setTextContent(String.valueOf(score.getType()));
                    scoreElement.appendChild(typeElement);

                    root.appendChild(scoreElement);
                }
                resultDOM.appendChild(root);

                body.addDocument(resultDOM);

                File soapFile = new File(filepath);
                message.saveChanges();
                message.writeTo(new FileOutputStream(soapFile));

                connection.close();
            }
        } catch (ParserConfigurationException | SOAPException | IOException e) {
            e.printStackTrace();
        }
    }
}
