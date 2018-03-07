package assignment9.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "部门")
public class Department {

    @XmlElement(name = "部门名称")
    protected String name;

    @XmlElement(name = "部门电话")
    protected String phone;

    @XmlElement(name = "部门地址")
    protected String address;

}
