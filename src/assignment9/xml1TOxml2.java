package assignment9;

import assignment9.model.Score;
import assignment9.model.Student;
import assignment9.model.StudentList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class xml1TOxml2 {

    public static void main(String[] args) {
        File xml1 = new File("src/assignment9/resource/XML文档1.xml");
        try {
            //读取xml1
            JAXBContext jaxbContext1 = JAXBContext.newInstance(Student.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();

            List<Student> students = new ArrayList<>();

            //从数据文件中读取 学生 信息
            FileReader reader = new FileReader("src/assignment9/resource/student_info.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);

            //略过开头标题行
            String line = bufferedReader.readLine();

            //对于每一行 学生 信息
            while ((line = bufferedReader.readLine()) != null) {
                Student student = (Student) jaxbUnmarshaller.unmarshal(xml1);

                String[] info = line.split(",");

                student.personInfo.name = info[0];
                student.personInfo.number = info[1];

                for (Score score : student.scoreList.scores) {
                    score.point = (int) (40 + Math.random() * 60);
                }

                students.add(student);
            }

            StudentList studentList = new StudentList();
            studentList.students = students;
            //写入xml2
            JAXBContext jaxbContext2 = JAXBContext.newInstance(StudentList.class);
            Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(studentList, new File("src/assignment9/resource/XML2.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
