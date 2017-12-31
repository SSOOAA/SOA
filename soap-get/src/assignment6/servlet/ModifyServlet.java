package assignment6.servlet;

import Utils.ModScore;
import Utils.ModifyXML;
import Utils.ParseSOAP;
import Utils.QueryScore;
import assignment5.model.Score;
import assignment6.model.ModifyScore;
import org.xml.sax.SAXException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;

@WebServlet("/modify")
public class ModifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        super.doGet(req, resp);
//        doPost(req,resp);

//        PrintWriter out = resp.getWriter();
//        out.println("classes项目全路径:"+this.getClass().getClassLoader().getResource("/").getPath());
//        out.println("web根的上下文路径："+req.getContextPath());
//        out.println("获得当前目录的路径:"+req.getRealPath(""));
//        out.println("项目发布路径:"+(String)req.getContextPath());
//        out.println("项目test真实路径："+req.getRequestURI());
//        out.println("项目真实路径："+req.getRealPath(req.getServletPath()));


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");

        //将成绩修改信息封装成SOAP消息
        String sid = req.getParameter("sid");
        String cid = req.getParameter("cid");
        String sType = req.getParameter("sType");
        String score = req.getParameter("score");
        ModifyScore modifyScore = new ModifyScore(sid,cid,score,sType);
        String modSoapPath = "C:\\Users\\Administrator\\IdeaProjects\\SOA\\soap-get\\src\\resource\\changeInfo.xml";

        ModScore modSOAP = new ModScore();
        modSOAP.CreateModScoreSOAP(modifyScore,modSoapPath);

        //解析成绩修改信息的SOAP消息
        SAXParser parser;
        ModifyScore modifyScore2 = null;
        try {
            //构建SAXParser
            parser = SAXParserFactory.newInstance().newSAXParser();
            //实例化  DefaultHandler对象
            ParseSOAP parseSoap=new ParseSOAP();
            //调用parse()方法
            parser.parse(new File("C:\\Users\\Administrator\\IdeaProjects\\SOA\\soap-get\\src\\resource\\changeInfo.xml"), parseSoap);
            //遍历结果
            modifyScore2 = parseSoap.getModifyScore();

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        //修改成绩
        ModifyXML modifyXML = new ModifyXML();
        String studentListPath = "C:\\Users\\Administrator\\IdeaProjects\\SOA\\soap-get\\src\\resource\\StudentList.xml";
        modifyXML.updateXML(modifyScore2,studentListPath);

        //获得修改后的成绩信息
        assert modifyScore2 != null;
        ArrayList<Score> scoreList = new QueryScore().findScore(modifyScore2.getSid(),studentListPath);

        //修改后的成绩的SOAP消息
        String queryPath = "C:\\Users\\Administrator\\IdeaProjects\\SOA\\soap-get\\src\\resource\\out.xml";
        QueryScore queryScore = new QueryScore();
        queryScore.createQueryScoreSOAP(scoreList,queryPath);

        //在界面上打印SOAP消息
        PrintWriter out = resp.getWriter();
        out.println("得到的SOAP消息为：");
        File soapFile = new File(queryPath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(soapFile), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(isr);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.replaceAll("<", "&lt");
            line = line.replaceAll(">", "&gt");
            out.println(line);
        }

        bufferedReader.close();


    }

}
