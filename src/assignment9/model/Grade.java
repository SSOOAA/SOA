package assignment9.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "成绩")
@XmlAccessorType(XmlAccessType.FIELD)
public class Grade {

    @XmlElement(name = "学号")
    private String sid;

    @XmlElement(name = "得分")
    private int score;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
