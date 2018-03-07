package assignment9.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "课程成绩")
public class CourseGrade {

    @XmlAttribute(name = "课程编号")
    private String courseId;

    @XmlAttribute(name = "成绩性质")
    private String scoreType;

    @XmlElements(value= {@XmlElement(name = "成绩",type = Grade.class)})
    private List<Grade> grades;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }


}
