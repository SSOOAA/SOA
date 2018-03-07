package assignment9.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "课程成绩列表")
@XmlAccessorType(XmlAccessType.FIELD)
public class CourseGradeList {

    @XmlElements(value = {@XmlElement(name = "课程成绩",type = CourseGrade.class) })
    private List<CourseGrade> courseGrades;

    public List<CourseGrade> getCourseGrades(){
        return courseGrades;
    }

    public void setCourseGrades(List<CourseGrade> list){
        courseGrades = list;
    }
}
