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
            //����SAXParser  
            parser = SAXParserFactory.newInstance().newSAXParser();  
            //ʵ����  DefaultHandler����  
            ParseXML parseXml=new ParseXML();   
            //����parse()����  
            parser.parse(new File("src/assignment4/XML�ĵ�3.xml"), parseXml);  
            //�������  
            ArrayList<Score> list=parseXml.getlist(); 
            
            CreateXML createXML = new CreateXML();
            createXML.createXML(list);
//            ArrayList<Score> slArrayList = createXML.findReserve(list);
//            Score score = slArrayList.get(0);
//            ArrayList<SingleScore> singleScores = score.getScoreList();
//            for(SingleScore score2:singleScores){
//            	System.out.println("ѧ�ţ�"+score2.getSid()+"  �÷�"+score2.getMark());
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
