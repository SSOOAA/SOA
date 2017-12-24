package assignment4;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateXML {
	public void createXML(ArrayList<Score> list){
		SAXTransformerFactory saxtransformerfactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		TransformerHandler transformerhandler = null;
		try {
			transformerhandler = saxtransformerfactory.newTransformerHandler();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Transformer transformer=transformerhandler.getTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		File file=new File("src/assignment4/XML文档4.xml");
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Result result = null;
		try {
			result = new StreamResult(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformerhandler.setResult(result);
		
		list = findReserve(list);
		
		 try {
			transformerhandler.startDocument();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 AttributesImpl atts = new AttributesImpl();
		 
		 for (Score score : list) {
			atts.clear();
			try {
				transformerhandler.startElement("", "", "课程成绩", atts);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 if (score.getScoreList() != null) {
				for(SingleScore score2:score.getScoreList()){
					try {
						transformerhandler.startElement("", "", "成绩", atts);
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (score2.getSid() != null) {
						atts.clear();
						try {
							transformerhandler.startElement("", "", "学号", atts);
							transformerhandler.characters(score2.getSid().toCharArray(), 0, score2.getSid().length());
							transformerhandler.endElement("", "", "学号");
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (score2.getMark() != null) {
						atts.clear();
						try {
							transformerhandler.startElement("", "", "得分", atts);
							transformerhandler.characters(score2.getMark().toCharArray(), 0, score2.getMark().length());
							transformerhandler.endElement("", "", "得分");
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						transformerhandler.endElement("", "", "成绩");
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			 
			 if (score.getCourseId() != null) {
				 atts.clear();
				 try {
					transformerhandler.startElement("", "", "课程编号", atts);
					transformerhandler.characters(score.getCourseId().toCharArray(), 0, score.getCourseId().length());
					transformerhandler.endElement("", "", "课程编号");
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			 if (score.getScoreType() != null) {
				 atts.clear();
				 try {
					transformerhandler.startElement("", "", "成绩性质", atts);
					transformerhandler.characters(score.getScoreType().toCharArray(), 0, score.getScoreType().length());
					transformerhandler.endElement("", "", "成绩性质");
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			 try {
				transformerhandler.endElement("", "", "课程成绩");
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		 try {
			transformerhandler.endDocument();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Score> findReserve(ArrayList<Score> list){
		ArrayList<Reserve> reserves = new ArrayList<Reserve>();
		ArrayList<Score> temp = new ArrayList<Score>();
		
		for(int i=0;i<list.size();i++){
			Score score = new Score();
			score.setCourseId(list.get(i).getCourseId());
			score.setScoreType(list.get(i).getScoreType());
			ArrayList<SingleScore> scores = new ArrayList<SingleScore>();
			for(int j=0;j<list.get(i).getScoreList().size();j++){
				SingleScore score2 = new SingleScore();
				score2.setSid(list.get(i).getScoreList().get(j).getSid());
				score2.setMark(list.get(i).getScoreList().get(j).getMark());
				scores.add(score2);
			}
			score.setScoreList(scores);
			temp.add(score);
		}
		
		for(Score score:list){
			for(SingleScore score2:score.getScoreList()){
				if (Integer.parseInt(score2.getMark())<60) {
					Reserve reserve = new Reserve();
					reserve.setSid(score2.getSid());
					reserve.setCid(score.getCourseId());
					if (reserves.indexOf(reserve) == -1) {
						reserves.add(reserve);
					}
				}
			}
		}
		
//		for(Reserve reserve:reserves){
//			System.out.println(reserve.getSid()+"  "+reserve.getCid());
//		}
		
		
		for(int i=0;i<list.size();i++){
			boolean flag = true;
			Score score = list.get(i);
			ArrayList<SingleScore> singleScores = score.getScoreList();
			for(SingleScore score2:singleScores){
				for(Reserve reserve:reserves){
					if (score2.getSid().equals(reserve.getSid()) && score.getCourseId().equals(reserve.getCid())) {
//						System.out.println(score2.getSid()+"  "+score.getCourseId());
						flag = false;
					}
				}
				if (flag) {
					System.out.println("y");
					temp.get(i).getScoreList().remove(score2);
				}
			}
		}
		return temp;
	}
}
