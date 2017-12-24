package assignment3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import assignment4.SchemaValidator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileReader;

public class StudentListXmlHelper {

    private static final String[] COURSE_ID_ARRAY = {"25010390", "25010570", "25010350", "25010110", "25010430"};
    private static final String[] COURSE_NAME_ARRAY = {"Linux程序设计", "服务计算与SOA开发", "J2EE与中间件", "软件系统设计与体系结构", "电子商务"};
    private static final String[] SCORE_TYPE_ARRAY = {"平时成绩", "期末成绩", "总评成绩"};

    private static final String NAMESPACE_JW = "http://jw.nju.edu.cn/schema";
    private static final String NAMESPACE_WWW = "http://www.nju.edu.cn/schema";

    private void generateXml() {
        try {
            //创建DOM构造器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);

            //获取DOM构造器
            DocumentBuilder builder = factory.newDocumentBuilder();

            //创建 学生列表 DOM
            Document studentListDOM = builder.newDocument();

            //创建 学生列表 并添加至DOM中
            Element root = studentListDOM.createElementNS(NAMESPACE_JW, "学生列表");
            root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:nju", NAMESPACE_WWW);
            root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance",
                    "xsi:schemaLocation", NAMESPACE_JW + " StudentList.xsd");
            studentListDOM.appendChild(root);

            //从数据文件中读取 学生 信息
            FileReader reader = new FileReader("src/assignment3/student_info.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);

            //略过开头标题行
            String line = bufferedReader.readLine();

            //对于每一行 学生 信息
            while ((line = bufferedReader.readLine()) != null) {
                String[] info = line.split(",");

                //创建 学生
                Element student = studentListDOM.createElement("学生");

                //创建 个人基本信息
                Element personalInfo = studentListDOM.createElement("个人基本信息");

                //创建 姓名 并添加至 个人基本信息
                Element name = studentListDOM.createElementNS(NAMESPACE_WWW, "姓名");
                name.setPrefix("nju");
                name.appendChild(studentListDOM.createTextNode(info[0]));
                personalInfo.appendChild(name);

                //创建 部门
                Element department = studentListDOM.createElementNS(NAMESPACE_WWW, "部门");
                department.setPrefix("nju");

                //创建 部门名称 并添加至 部门 中
                Element departmentName = studentListDOM.createElementNS(NAMESPACE_WWW, "部门名称");
                departmentName.setPrefix("nju");
                departmentName.appendChild(studentListDOM.createTextNode("软件学院"));
                department.appendChild(departmentName);

                //创建 部门电话 并添加至 部门 中
                Element departmentContact = studentListDOM.createElementNS(NAMESPACE_WWW, "部门电话");
                departmentContact.setPrefix("nju");
                departmentContact.appendChild(studentListDOM.createTextNode("025-83621002"));
                department.appendChild(departmentContact);

                //创建 部门地址 并添加至 部门 中
                Element departmentAddress = studentListDOM.createElementNS(NAMESPACE_WWW, "部门地址");
                departmentAddress.setPrefix("nju");
                departmentAddress.appendChild(studentListDOM.createTextNode("南京市汉口路22号，南京大学软件学院(费彝民楼B座4-9层)"));
                department.appendChild(departmentAddress);

                //添加 部门 至 个人基本信息 中
                personalInfo.appendChild(department);

                //创建 职务 至 个人基本信息 中
                Element type = studentListDOM.createElementNS(NAMESPACE_WWW, "职务");
                type.setPrefix("nju");
                type.appendChild(studentListDOM.createTextNode("本科生"));
                personalInfo.appendChild(type);

                //创建 个人编号 至 个人基本信息 中
                Element id = studentListDOM.createElementNS(NAMESPACE_WWW, "个人编号");
                id.setPrefix("nju");
                id.appendChild(studentListDOM.createTextNode(info[1]));
                personalInfo.appendChild(id);

                //添加 个人基本信息 至 学生 中
                student.appendChild(personalInfo);

                //创建 课程列表
                Element courseList = studentListDOM.createElement("课程列表");

                //对于每一门课
                for (int i = 0; i < COURSE_ID_ARRAY.length; i++) {
                    //创建 课程编号 并添加至 课程列表 中
                    Element courseId = studentListDOM.createElement("课程编号");
                    courseId.appendChild(studentListDOM.createTextNode(COURSE_ID_ARRAY[i]));
                    courseList.appendChild(courseId);

                    //创建 课程名 并添加至 课程列表 中
                    Element courseName = studentListDOM.createElement("课程名");
                    courseName.appendChild(studentListDOM.createTextNode(COURSE_NAME_ARRAY[i]));
                    courseList.appendChild(courseName);
                }

                //添加 课程列表 至 学生 中
                student.appendChild(courseList);

                //创建 学生成绩列表
                Element scoreList = studentListDOM.createElement("学生成绩列表");

                //对于每一门课，每一类成绩
                for (int i = 0; i < COURSE_ID_ARRAY.length; i++) {
                    for (int j = 0; j < SCORE_TYPE_ARRAY.length; j++) {
                        //创建 学生成绩
                        Element score = studentListDOM.createElement("学生成绩");

                        //创建 得分 并添加至 学生成绩 中
                        Element scorePoint = studentListDOM.createElement("得分");
                        //得分为0-100的随机数
                        scorePoint.appendChild(studentListDOM.createTextNode(String.valueOf((int) (40 + Math.random() * 60))));
                        score.appendChild(scorePoint);

                        //创建 课程编号 并添加至 学生成绩 中
                        Element courseId = studentListDOM.createElement("课程编号");
                        courseId.appendChild(studentListDOM.createTextNode(COURSE_ID_ARRAY[i]));
                        score.appendChild(courseId);

                        //创建 成绩性质 并添加至 学生成绩中
                        Element scoreType = studentListDOM.createElement("成绩性质");
                        scoreType.appendChild(studentListDOM.createTextNode(SCORE_TYPE_ARRAY[j]));
                        score.appendChild(scoreType);

                        //添加 学生成绩 至 学生成绩 列表中
                        scoreList.appendChild(score);
                    }
                }

                //添加 学生成绩列表 至 学生 中
                student.appendChild(scoreList);

                //添加 学生 至 学生列表 中
                root.appendChild(student);
            }


            //创建DOM转换器工厂
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            //获取DOM转换器
            Transformer transformer = transformerFactory.newTransformer();

            //设置输出的xml文件编码为UTF-8
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");

            //设置输出的xml文件为缩进格式
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //由转换器把DOM资源转换到结果输出流，结果输出流连接到一个xml文件
            transformer.transform(new DOMSource(studentListDOM), new StreamResult("src/assignment3/StudentList.xml"));

            if (SchemaValidator.validateSchema("src/assignment2/StudentList.xsd", "src/assignment3/StudentList.xml")) {
                System.out.println("成功：输出的StudentList.xml符合schema！");
            } else {
                System.out.println("失败：输出的StudentList.xml不符合schema！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        StudentListXmlHelper helper = new StudentListXmlHelper();
        helper.generateXml();

    }
}
