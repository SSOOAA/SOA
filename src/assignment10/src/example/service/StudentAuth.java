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
public class StudentAuth {

    private Document loadData() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new File("data/studentAuth.xml"));
    }

    public String emailAuthOperation (EmailAuthMsg emailAuthMsg) throws InputFault, IOException, SAXException, ParserConfigurationException {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateValue(emailAuthMsg.account)) {
            inputFaultType.setMsg("重新填写邮箱账号");
            throw new InputFault(inputFaultType);
        }
        Document document = loadData();
        NodeList studentList=document.getElementsByTagName("学生");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            Element emailAccount = (Element) student.getElementsByTagName("邮箱").item(0);
            Element emailPassword = (Element)student.getElementsByTagName("邮箱密码").item(0);
            if (emailAuthMsg.account.equals(emailAccount.getTextContent()) && emailAuthMsg.password.equals(emailPassword.getTextContent())) {
                return student.getElementsByTagName("个人编号").item(0).getTextContent();
            }
        }
        return null;
    }

    public String studentIDAuthOperation(StudentIDAuthMsg studentIDAuthMsg) throws InputFault, IOException, SAXException, ParserConfigurationException {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateValue(studentIDAuthMsg.id)) {
            inputFaultType.setMsg("需要个人编号");
            throw new InputFault(inputFaultType);
        }
        Document document = loadData();
        NodeList studentList=document.getElementsByTagName("学生");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            Element studentID = (Element) student.getElementsByTagName("个人编号").item(0);
            Element pwd = (Element)student.getElementsByTagName("密码").item(0);
            if (studentIDAuthMsg.id.equals(studentID.getTextContent()) && studentIDAuthMsg.pwd.equals(pwd.getTextContent())) {
                return studentID.getTextContent();
            }
        }
        return null;
    }

    private boolean validateValue(String value){
        return value != null && !value.isEmpty();
    }
}
