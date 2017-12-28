package assignment6.servlet;

import assignment5.model.Score;
import assignment5.sax.MyContentHandler;
import assignment6.model.ModifyScore;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.*;
import javax.xml.soap.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet("/modify")
public class ModifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        String line;
        StringBuilder sb = new StringBuilder();
        PrintWriter out = resp.getWriter();
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//            out.println(line);
            sb.append(line);
        }
        out.println(sb.toString());

        ModifyScore modifyScore = new ModifyScore();
        try {
            MessageFactory msgFactory;
            msgFactory = MessageFactory.newInstance();
            SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
            reqMsg.saveChanges();
            SOAPBody body = reqMsg.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();

            while (iterator.hasNext()) {
                SOAPElement element = (SOAPElement) iterator.next();
//                if (element == null){
//                    out.println("fail");
//                }else {
//                    out.println(element.getTagName());
//                }
//                out.println("tag:"+element.getTagName()+" value:"+element.getValue());
                if (element.getTagName().equals("个人编号")) {
                    modifyScore.setSid(element.getValue());
                }
                if (element.getTagName().equals("课程编号")) {
                    modifyScore.setCid(element.getValue());
                }
                if (element.getTagName().equals("得分")) {
                    modifyScore.setScore(Integer.parseInt(element.getValue()));
                }
                if (element.getTagName().equals("成绩性质")) {
                    modifyScore.setScoreType(element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModifyXMLDom modifyXMLDom = new ModifyXMLDom();
        modifyXMLDom.updateXML(modifyScore);

        String sid = modifyScore.getSid();
        ArrayList<Score> scoreList = new ArrayList<>();

        //使用sax解析StudentList.xml获取成绩信息
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new MyContentHandler(sid, scoreList));
            xmlReader.setErrorHandler(new DefaultErrorHandler());
            xmlReader.parse(getClass().getResource("StudentList.xml").getPath());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        //创建SOAP消息
        try {
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
            }

            File soapFile = new File(getClass().getResource("out.xml").getPath());
            message.writeTo(new FileOutputStream(soapFile));

            Document doc = (Document) message.getSOAPPart().getEnvelope().getOwnerDocument();
            StringWriter output = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform( new DOMSource((org.w3c.dom.Node) doc), new StreamResult(output));
            out.write(output.toString());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

}
