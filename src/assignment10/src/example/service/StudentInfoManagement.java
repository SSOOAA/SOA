package example.service;

import example.service.fault.InputFault;
import example.service.fault.InputFaultType;
import example.service.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@WebService
public class StudentInfoManagement {

    private Document loadData() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new File("data/score.xml"));
    }

    public StudentInfo retrieveStudentInfoOperation(String studentToken)throws ParserConfigurationException, IOException, SAXException,InputFault {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateValue(studentToken)) {
            inputFaultType.setMsg("需要学生编号");
            throw new InputFault(inputFaultType);
        }
        Document document = loadData();
        NodeList studentList=document.getElementsByTagName("学生");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
            if (studentToken.equals(studentNumber.getTextContent())) {
              StudentInfo studentInfo=new StudentInfo();
              studentInfo.name=student.getElementsByTagName("姓名").item(0).getTextContent();
              Department department=new Department();
              department.name=student.getElementsByTagName("部门名称").item(0).getTextContent();
              department.phoneNumber=student.getElementsByTagName("部门电话").item(0).getTextContent();
              department.address=student.getElementsByTagName("部门地址").item(0).getTextContent();
              studentInfo.department=department;
              JobType jobType=getJobType(student.getElementsByTagName("职务").item(0).getTextContent());
              studentInfo.jobType=jobType;
              return studentInfo;
            }
        }
        return null;
    }

    public String modifyStudentInfoOperation(String studentToken, StudentInfo studentInfo)throws ParserConfigurationException, IOException, SAXException,InputFault{
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateValue(studentToken)) {
            inputFaultType.setMsg("需要编号");
            throw new InputFault(inputFaultType);
        }
        if (!validateValue(studentInfo.name)) {
            inputFaultType.setMsg("错误的姓名");
            throw new InputFault(inputFaultType);
        }
        if (!(validateValue(studentInfo.department.name)&&validateValue(studentInfo.department.address))) {
            inputFaultType.setMsg("错误的部门信息");
            throw new InputFault(inputFaultType);
        }
        if(studentInfo.department.phoneNumber.length()!=11){
            inputFaultType.setMsg("错误的部门号码");
            throw new InputFault(inputFaultType);
        }
        if (!validateValue(studentInfo.jobType.toString())) {
            inputFaultType.setMsg("错误的职务信息");
            throw new InputFault(inputFaultType);
        }
        Document document = loadData();
        NodeList studentList=document.getElementsByTagName("学生");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
            if (studentToken.equals(studentNumber.getTextContent())) {
                student.getElementsByTagName("姓名").item(0).setTextContent(studentInfo.name);
                student.getElementsByTagName("部门名称").item(0).setTextContent(studentInfo.department.name);
                student.getElementsByTagName("部门电话").item(0).setTextContent(studentInfo.department.phoneNumber);
                student.getElementsByTagName("部门地址").item(0).setTextContent(studentInfo.department.address);
                student.getElementsByTagName("职务").item(0).setTextContent(getJobTypeString(studentInfo.jobType.toString()));
                return "SUCCESS";
            }
        }
        return "FAIL";
    }

    private JobType getJobType(String jobType){
        switch (jobType){
            case "本科生": return JobType.undergraduate;
            case "硕士生": return JobType.postgraduate;
            case "博士生": return JobType.doctor;
            case "教工": return JobType.teacher;
            default: return null;
        }
    }

    private String getJobTypeString(String jobType){
        switch (jobType){
            case "undergraduate" : return "本科生";
            case "postgraduate": return "硕士生";
            case "doctor": return "博士生";
            case "teacher": return "教工";
            default: return null;
        }
    }

    private boolean validateValue(String value) {
        return value != null && !value.isEmpty();
    }
}
