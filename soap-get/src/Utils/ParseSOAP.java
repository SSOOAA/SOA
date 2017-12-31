package Utils;

import assignment6.model.ModifyScore;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseSOAP extends DefaultHandler {
    private ModifyScore modifyScore;
    private String tagName;
    private StringBuilder stringBuilder;

    public ModifyScore getModifyScore() {
        return this.modifyScore;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setModifyScore(ModifyScore modifyScore) {
        this.modifyScore = modifyScore;
    }

    @Override
    public void startDocument() throws SAXException {
//        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("成绩修改信息")){
            modifyScore = new ModifyScore();
        }
        this.tagName = qName;
        stringBuilder = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        tagName = null;

        if (qName.equals("个人编号")){
            modifyScore.setSid(stringBuilder.toString());
        }
        if (qName.equals("课程编号")){
            modifyScore.setCid(stringBuilder.toString());
        }
        if (qName.equals("得分")){
            modifyScore.setScore(stringBuilder.toString());
        }
        if (qName.equals("成绩性质")){
            modifyScore.setScoreType(stringBuilder.toString());
        }
    }

    @Override
    public void endDocument() throws SAXException {
//        super.endDocument();
        tagName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stringBuilder.append(new String(ch,start,length));
    }

}
