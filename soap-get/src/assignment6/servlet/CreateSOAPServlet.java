package assignment6.servlet;

import assignment5.model.Score;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.*;

@WebServlet("/createSOAP")
public class CreateSOAPServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("sid");
        String cid = req.getParameter("cid");
        String sType = req.getParameter("sType");
        String score = req.getParameter("score");

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");

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
            sidDom.setTextContent(sid);
            root.appendChild(sidDom);

            Element scoreDom = resultDOM.createElement("得分");
            scoreDom.setTextContent(score);
            root.appendChild(scoreDom);

            Element typeDom = resultDOM.createElement("成绩性质");
            typeDom.setTextContent(sType);
            root.appendChild(typeDom);

            Element cidDom = resultDOM.createElement("课程编号");
            cidDom.setTextContent(cid);
            root.appendChild(cidDom);

            resultDOM.appendChild(root);

            SOAPBody.addDocument(resultDOM);

            File soapFile = new File(getClass().getResource("changeInfo.xml").getPath());
            message.saveChanges();
            message.writeTo(new FileOutputStream(soapFile));

            InputStreamReader isr = new InputStreamReader(new FileInputStream(soapFile), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = null;
            PrintWriter out = resp.getWriter();
            out.println("Content-Type: application/soap+xml; charset=\"utf-8\" \n");
            while ((line = bufferedReader.readLine()) != null) {
                out.println(line);
            }
            bufferedReader.close();

            connection.close();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
}
