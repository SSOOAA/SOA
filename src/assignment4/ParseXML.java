package assignment4;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ParseXML extends DefaultHandler{
	//存放遍历集合  
    private ArrayList<Score> list;
    private Score score;
    private String tagName;
    private ArrayList<SingleScore> scoreList;
    private SingleScore singleScore;
    
	public ArrayList<Score> getlist() {
		return list;
	}
	public void setlist(ArrayList<Score> scoreList) {
		this.list = scoreList;
	}
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
    
	 public ArrayList<SingleScore> getScoreList() {
		return scoreList;
	}
	public void setScoreList(ArrayList<SingleScore> scoreList) {
		this.scoreList = scoreList;
	}
	public SingleScore getSingleScore() {
		return singleScore;
	}
	public void setSingleScore(SingleScore singleScore) {
		this.singleScore = singleScore;
	}
	//只调用一次  初始化list集合    
    @Override  
    public void startDocument() throws SAXException {  
        list=new ArrayList<Score>();
        scoreList=new ArrayList<SingleScore>();
    }  
    
  //调用多次    开始解析  
    @Override  
    public void startElement(String uri, String localName, String qName,  
            Attributes attributes) throws SAXException {  
        if(qName.equals("课程成绩")){  
            score=new Score();
            score.setCourseId(attributes.getValue(0));
            score.setScoreType(attributes.getValue(1));
        }
        if(qName.equals("成绩")){
        	singleScore = new SingleScore();
        }
        this.tagName=qName;  
    }  
    
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
         if (score != null && "课程成绩".equals(name)) {
             score.setScoreList(scoreList);
             list.add(score);
             score = null;
             scoreList= new ArrayList<SingleScore>();
         }
         if (singleScore != null && "成绩".equals(name)) {
        	 scoreList.add(singleScore);
             singleScore = null;
         }
         tagName = null;
    }
      
      
    //只调用一次  
    @Override  
    public void endDocument() throws SAXException {  
    }  
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (score != null) {
            String data = new String(ch, start, length);
            if ("学号".equals(tagName)) {
                singleScore.setSid(data);
            }
            if ("得分".equals(tagName)) {
                singleScore.setMark(data);
            }
        }
    }
    
    
      

}
