package assignment4;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {  
        SAXParser parser = null;  
        try {  
            //构建SAXParser  
            parser = SAXParserFactory.newInstance().newSAXParser();  
            //实例化  DefaultHandler对象  
            ParseXML parseXml=new ParseXML();   
            //调用parse()方法  
            parser.parse(new File("src/assignment4/XML文档3.xml"), parseXml);  
            //遍历结果  
            ArrayList<Score> list=parseXml.getlist(); 
            
            CreateXML createXML = new CreateXML();
            createXML.createXML(list);
//            ArrayList<Score> slArrayList = createXML.findReserve(list);
//            Score score = slArrayList.get(0);
//            ArrayList<SingleScore> singleScores = score.getScoreList();
//            for(SingleScore score2:singleScores){
//            	System.out.println("学号："+score2.getSid()+"  得分"+score2.getMark());
//            }
        } catch (ParserConfigurationException e) {  
            e.printStackTrace();  
        } catch (SAXException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
