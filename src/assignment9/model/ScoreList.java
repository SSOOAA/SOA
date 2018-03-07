package assignment9.model;

import assignment9.model.Score;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "学生成绩列表")
public class ScoreList {

    @XmlElement(name = "学生成绩")
    public List<Score> scores;

}
