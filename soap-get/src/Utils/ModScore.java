package Utils;

import assignment6.model.ModifyScore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModScore {

    public void CreateModScoreSOAP(ModifyScore modifyScore, String filepath){
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
            SOAPBody SOAPBody = envelope.getBody();

            //讲成绩修改信息放在一个DOM里
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document resultDOM = documentBuilder.newDocument();

            Element root = resultDOM.createElement("成绩修改信息");

            Element sidDom = resultDOM.createElement("个人编号");
            sidDom.setTextContent(modifyScore.getSid());
            root.appendChild(sidDom);

            Element scoreDom = resultDOM.createElement("得分");
            scoreDom.setTextContent(modifyScore.getScore());
            root.appendChild(scoreDom);

            Element typeDom = resultDOM.createElement("成绩性质");
            typeDom.setTextContent(modifyScore.getScoreType());
            root.appendChild(typeDom);

            Element cidDom = resultDOM.createElement("课程编号");
            cidDom.setTextContent(modifyScore.getCid());
            root.appendChild(cidDom);

            resultDOM.appendChild(root);

            SOAPBody.addDocument(resultDOM);

            File soapFile = new File(filepath);
            message.saveChanges();
            message.writeTo(new FileOutputStream(soapFile));

            connection.close();
        } catch (SOAPException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

}
