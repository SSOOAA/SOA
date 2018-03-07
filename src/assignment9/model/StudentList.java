package assignment9.model;

import assignment9.model.Student;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "学生列表")
public class StudentList {

    @XmlElement(name = "学生")
    public List<Student> students;
}
