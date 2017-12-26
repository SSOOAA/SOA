package assignment4;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ParseXML extends DefaultHandler{
	//��ű�������  
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
	//ֻ����һ��  ��ʼ��list����    
    @Override  
    public void startDocument() throws SAXException {  
        list=new ArrayList<Score>();
        scoreList=new ArrayList<SingleScore>();
    }  
    
  //���ö��    ��ʼ����  
    @Override  
    public void startElement(String uri, String localName, String qName,  
            Attributes attributes) throws SAXException {  
        if(qName.equals("�γ̳ɼ�")){  
            score=new Score();
            score.setCourseId(attributes.getValue(0));
            score.setScoreType(attributes.getValue(1));
        }
        if(qName.equals("�ɼ�")){
        	singleScore = new SingleScore();
        }
        this.tagName=qName;  
    }  
    
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
         if (score != null && "�γ̳ɼ�".equals(name)) {
             score.setScoreList(scoreList);
             list.add(score);
             score = null;
             scoreList= new ArrayList<SingleScore>();
         }
         if (singleScore != null && "�ɼ�".equals(name)) {
        	 scoreList.add(singleScore);
             singleScore = null;
         }
         tagName = null;
    }
      
      
    //ֻ����һ��  
    @Override  
    public void endDocument() throws SAXException {  
    }  
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (score != null) {
            String data = new String(ch, start, length);
            if ("ѧ��".equals(tagName)) {
                singleScore.setSid(data);
            }
            if ("�÷�".equals(tagName)) {
                singleScore.setMark(data);
            }
        }
    }
    
    
      

}
