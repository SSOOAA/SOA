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
import java.util.LinkedList;
import java.util.List;

@WebService
public class ScoreManagement {

    private Document loadData() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new File("data/score.xml"));
    }

    public List<QueryResult> queryScoreOperation(List<String> sidList) throws ParserConfigurationException, IOException, SAXException, InputFault {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateList(sidList)) {
            inputFaultType.setMsg("至少需要一个学号");
            throw new InputFault(inputFaultType);
        }

        List<QueryResult> resultList = new LinkedList<>();
        Document document = loadData();

        NodeList studentList = document.getElementsByTagName("学生");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
            if (sidList.contains(studentNumber.getTextContent())) {
                QueryResult result = new QueryResult();
                result.sid = studentNumber.getTextContent();
                NodeList scores = student.getElementsByTagName("学生成绩");
                for (int j = 0; j < scores.getLength(); j++) {
                    Element score = (Element) scores.item(j);
                    Score data = new Score();
                    data.point = Integer.parseInt(score.getElementsByTagName("得分").item(0).getTextContent());
                    data.cid = score.getElementsByTagName("课程编号").item(0).getTextContent();
                    data.type = score.getElementsByTagName("成绩性质").item(0).getTextContent();
                    result.scoreList.add(data);
                }
                resultList.add(result);
            }
        }
        if (resultList.isEmpty()) {
            inputFaultType.setMsg("学号不存在");
            throw new InputFault(inputFaultType);
        }
        return resultList;
    }

    public RequestResult deleteScoreOperation(List<DeleteScore> deleteScoreList) throws IOException, SAXException, ParserConfigurationException, InputFault {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateList(deleteScoreList)) {
            inputFaultType.setMsg("至少需要一个学生成绩");
            throw new InputFault(inputFaultType);
        }
        //去除不合法的数据
        for (DeleteScore deleteScore : deleteScoreList) {
            if (validateValue(deleteScore.sid)) {
                deleteScoreList.remove(deleteScore);
            }
        }
        if (!validateList(deleteScoreList)) {
            inputFaultType.setMsg("无有效的学号");
            throw new InputFault(inputFaultType);
        }
        RequestResult result = new RequestResult();
        Document document = null;
        try {
            document = loadData();
            NodeList studentList = document.getElementsByTagName("学生");
            for (int i = 0; i < studentList.getLength(); i++) {
                Element student = (Element) studentList.item(i);
                Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
                for (DeleteScore deleteScore : deleteScoreList) {
                    if (deleteScore.sid.equals(studentNumber.getTextContent())) {
                        NodeList scoreList = student.getElementsByTagName("学生成绩");
                        for (int j = 0; j < scoreList.getLength(); j++) {
                            Element score = (Element) scoreList.item(j);
                            if (deleteScore.cid.equals(score.getElementsByTagName("课程编号").item(0).getTextContent())
                                    && deleteScore.scoreType.equals(score.getElementsByTagName("成绩性质").item(0).getTextContent())) {
                                student.removeChild(score);
                            }
                        }
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            result.isSuccess = false;
            result.msg = e.getMessage();
            throw e;
        }
        result.isSuccess = true;
        return result;
    }

    public RequestResult modifyScoreOperation(List<ModifyScore> modifyScoreList) throws InputFault, IOException, SAXException, ParserConfigurationException {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateList(modifyScoreList)) {
            inputFaultType.setMsg("至少需要一个学生成绩");
            throw new InputFault(inputFaultType);
        }
        //去除不合法的数据
        for (ModifyScore modifyScore : modifyScoreList) {
            if (!validateValue(modifyScore.sid)) {
                modifyScoreList.remove(modifyScore);
            }
        }
        if (!validateList(modifyScoreList)) {
            inputFaultType.setMsg("无有效的学号");
            throw new InputFault(inputFaultType);
        }
        RequestResult result = new RequestResult();
        Document document = null;
        try {
            document = loadData();
            NodeList studentList = document.getElementsByTagName("学生");
            for (int i = 0; i < studentList.getLength(); i++) {
                Element student = (Element) studentList.item(i);
                Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
                for (ModifyScore modifyScore : modifyScoreList) {
                    if (modifyScore.sid.equals(studentNumber.getTextContent())) {
                        NodeList scoreList = student.getElementsByTagName("学生成绩");
                        for (int j = 0; j < scoreList.getLength(); j++) {
                            Element score = (Element) scoreList.item(j);
                            if (modifyScore.newScore.point != null) {
                                score.getElementsByTagName("得分").item(0).setTextContent(modifyScore.newScore.point.toString());
                            }
                            if (validateValue(modifyScore.newScore.type)) {
                                score.getElementsByTagName("成绩性质").item(0).setTextContent(modifyScore.newScore.type);
                            }
                            if (validateValue(modifyScore.newScore.cid)) {
                                score.getElementsByTagName("课程编号").item(0).setTextContent(modifyScore.newScore.cid);
                            }
                        }
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            result.isSuccess = false;
            result.msg = e.getMessage();
            throw e;
        }
        result.isSuccess = true;
        return result;
    }

    public RequestResult addScoreOperation(List<AddScore> addScoreList) throws InputFault, IOException, SAXException, ParserConfigurationException {
        InputFaultType inputFaultType = new InputFaultType();
        if (!validateList(addScoreList)) {
            inputFaultType.setMsg("至少需要一个学生成绩");
            throw new InputFault(inputFaultType);
        }
        //去除不合法的数据
        for (AddScore addScore : addScoreList) {
            if (validateValue(addScore.sid)) {
                addScoreList.remove(addScore);
            }
        }
        if (!validateList(addScoreList)) {
            inputFaultType.setMsg("无有效的学号");
            throw new InputFault(inputFaultType);
        }
        RequestResult result = new RequestResult();
        Document document = null;
        try {
            document = loadData();
            NodeList studentList = document.getElementsByTagName("学生");

            for (int i = 0; i < studentList.getLength(); i++) {
                Element student = (Element) studentList.item(i);
                Element studentNumber = (Element) student.getElementsByTagName("个人编号").item(0);
                for (AddScore addScore : addScoreList) {
                    if (addScore.sid.equals(studentNumber.getTextContent())) {
                        boolean isExist = false;
                        NodeList scores = student.getElementsByTagName("学生成绩");
                        for (int j = 0; j < scores.getLength(); j++) {
                            Element score = (Element) scores.item(j);
                            //对应学号已存在相应成绩则跳过
                            if (addScore.newScore.cid.equals(score.getElementsByTagName("课程编号").item(0).getTextContent())
                                    && addScore.newScore.type.equals(score.getElementsByTagName("成绩性质").item(0).getTextContent())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            Element newScore = document.createElement("学生成绩");
                            Element newScorePoint = document.createElement("得分");
                            newScorePoint.setTextContent(addScore.newScore.point.toString());
                            Element newScoreCid = document.createElement("课程编号");
                            newScoreCid.setTextContent(addScore.newScore.cid);
                            Element newScoreType = document.createElement("成绩性质");
                            newScoreType.setTextContent(addScore.newScore.type);
                            newScore.appendChild(newScorePoint);
                            newScore.appendChild(newScoreCid);
                            newScore.appendChild(newScoreType);
                            student.getElementsByTagName("学生成绩列表").item(0).appendChild(newScore);
                        }
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            result.isSuccess = false;
            result.msg = e.getMessage();
            throw e;
        }
        result.isSuccess = true;
        return result;
    }

    private boolean validateList(List list) {
        return list != null && !list.isEmpty();
    }

    private boolean validateValue(String value) {
        return value != null && !value.isEmpty();
    }

}
