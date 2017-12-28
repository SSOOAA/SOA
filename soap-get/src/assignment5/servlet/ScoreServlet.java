package assignment5.servlet;

import assignment5.model.Score;
import assignment5.sax.MyContentHandler;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.*;
import javax.xml.soap.*;
import java.io.*;
import java.util.ArrayList;

@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SOAPConnection connection;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
        } catch (SOAPException e) {
            e.printStackTrace();
        }

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设定响应的内容格式

        String a = request.getParameter("query");
        String b = request.getParameter("download");

        if (a != null && a.equals("查询成绩")) {
            response.setContentType("text/html;charset=utf-8");
        } else if (b != null && b.equals("下载成绩单")) {
            response.setHeader("Content-Type", "Application/soap+xml;charset=utf-8");
        }

        String studentId = request.getParameter("id");
        ArrayList<Score> scoreList = new ArrayList<>();

        //使用sax解析StudentList.xml获取成绩信息
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new MyContentHandler(studentId, scoreList));
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
                Document resultDOM = documentBuilder.newDocument();
                Element root = resultDOM.createElement("学生成绩列表");

                for (Score score : scoreList) {
                    Element scoreElement = resultDOM.createElement("学生成绩");

                    Element pointElement = resultDOM.createElement("得分");
                    pointElement.setTextContent(String.valueOf(score.getPoint()));
                    scoreElement.appendChild(pointElement);

                    Element courseIdElement = resultDOM.createElement("课程编号");
                    courseIdElement.setTextContent(String.valueOf(score.getCourseId()));
                    scoreElement.appendChild(courseIdElement);

                    Element typeElement = resultDOM.createElement("成绩性质");
                    typeElement.setTextContent(String.valueOf(score.getType()));
                    scoreElement.appendChild(typeElement);

                    root.appendChild(scoreElement);
                }
                resultDOM.appendChild(root);

                body.addDocument(resultDOM);
            }

            File soapFile = new File(getClass().getResource("out.xml").getPath());
            message.writeTo(new FileOutputStream(soapFile));

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<p>当前学号：" + studentId + "</p>");
                out.println("<p>成绩列表如下：</p>");
                out.println("<table border='1'>");
                out.println("<tr><th>课程编号</th><th>成绩性质</th><th>得分</th></tr>");
                for (Score score : scoreList) {
                    out.print("<tr>");
                    out.print("<td>" + score.getCourseId() + "</td>");
                    out.print("<td>" + score.getType() + "</td>");
                    out.print("<td>" + score.getPoint() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            out.println("<h3>得到的soap消息为：</h3>");
            out.println("<p>");

//            if (!scoreList.isEmpty()) {
//                out.println("HTTP/1.1 200 OK\n");
//                out.println("Content-Type: application/soap+xml; charset=\"utf-8\" \n");
////                out.println("Content-Length: nnnn \n");
//            } else {
//                out.println("HTTP/1.1 500 Internal Server Error \n");
//                out.println("Content-Type: application/soap+xml; charset=\"utf-8\" \n");
////                out.println("Content-Length: nnnn \n");
//            }

            InputStreamReader isr = new InputStreamReader(new FileInputStream(soapFile), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = null;
            if (a != null && a.equals("查询成绩")) {
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.replaceAll("<", "&lt");
                    line = line.replaceAll(">", "&gt");
                    out.println(line);
                }
            } else if (b != null && b.equals("下载成绩单")) {
                while ((line = bufferedReader.readLine()) != null) {
                    out.println(line);
                }
            }

            bufferedReader.close();
            out.println("</p>");
            out.println("</body></html>");
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
