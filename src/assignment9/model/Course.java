package assignment9.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "课程")
public class Course {

    @XmlElement(name = "课程编号")
    protected String number;

    @XmlElement(name = "课程名")
    protected String name;

}
