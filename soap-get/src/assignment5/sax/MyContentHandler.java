package assignment5.sax;

import assignment5.model.Score;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class MyContentHandler extends DefaultHandler {

    private String studentId;
    private List<Score> scoreList;

    //记录当前标签
    private String currentTag = null;

    //是否找到对应学号的学生
    private boolean isStudentFound = false;

    //当前解析位置是否处于成绩列表中
    private boolean isScoreList = false;

    //当前成绩
    private Score currentScore;

    public MyContentHandler(String studentId, List<Score> scoreList) {
        this.studentId = studentId;
        this.scoreList = scoreList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = qName;

        //如果对应学号的学生已经找到
        if (isStudentFound) {
            if ("学生成绩列表".equals(currentTag)) {
                isScoreList = true;
            }
            else if ("学生成绩".equals(currentTag)) {
                currentScore = new Score();
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //如果对应学号的学生已经找到
        if (isStudentFound) {
            //每解析完一个学生成绩添加至成绩列表
            if ("学生成绩".equals(qName)) {
                scoreList.add(currentScore);
                currentScore = null;
            }
            //找到相应学生的全部成绩后抛异常结束sax解析
            else if ("学生成绩列表".equals(qName)) {
                throw new SAXException("解析结束");
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length);
        if (!content.trim().isEmpty()) {
            //如果还没有找到对应学号的学生
            if (!isStudentFound && "个人编号".equals(currentTag)) {
                isStudentFound = content.equals(studentId);
            }
            //如果当前解析位置处于成绩列表之中
            else if (isScoreList) {
                switch (currentTag) {
                    case "得分":
                        currentScore.setPoint(Integer.parseInt(content));
                        break;
                    case "课程编号":
                        currentScore.setCourseId(content);
                        break;
                    case "成绩性质":
                        currentScore.setType(content);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
