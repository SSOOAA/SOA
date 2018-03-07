package assignment9.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "学生成绩")
public class Score {

    @XmlElement(name = "得分")
    public int point;

    @XmlElement(name = "课程编号")
    public String number;

    @XmlElement(name = "成绩性质")
    public String type;
}
