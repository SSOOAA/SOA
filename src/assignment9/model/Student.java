package assignment9.model;

import assignment9.model.Course;
import assignment9.model.PersonInfo;
import assignment9.model.ScoreList;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "学生")
public class Student {

    @XmlElement(name = "个人基本信息")
    public PersonInfo personInfo;

    @XmlElementWrapper(name = "课程列表")
    @XmlElement(name = "课程")
    protected List<Course> courseList;

    @XmlElement(name = "学生成绩列表")
    public ScoreList scoreList;
}
