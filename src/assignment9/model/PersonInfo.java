package assignment9.model;

import assignment9.model.Department;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "个人基本信息")
public class PersonInfo {

    @XmlElement(name = "姓名")
    public String name;

    @XmlElement(name = "部门")
    protected Department department;

    @XmlElement(name = "职务")
    protected String career;

    @XmlElement(name = "个人编号")
    public String number;

}
