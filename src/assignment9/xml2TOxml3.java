package assignment9;

import assignment9.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class xml2TOxml3 {
    public static void main(String[]args){
        File xml2 = new File("src/assignment9/resource/XML2.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StudentList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StudentList studentList = (StudentList)unmarshaller.unmarshal(xml2);
            List<Student> students = studentList.students;

            List<String> cids = new ArrayList<String>();
            List<String> types = new ArrayList<String>();

            //第一遍循环找出所有课程编号和成绩性质
            for(Student student:students){
                ScoreList scoreList = student.scoreList;
                List<Score> scores = scoreList.scores;
                for(Score score:scores){
                    String cid = score.number;
                    String type = score.type;
                    if(!cids.contains(cid))
                        cids.add(cid);
                    if(!types.contains(type))
                        types.add(type);
                }
            }

            CourseGradeList courseGradeList = new CourseGradeList();
            List<CourseGrade> courseGrades = new ArrayList<CourseGrade>();
            //第二遍循环根据课程编号和成绩性质找到所有成绩
            for (String c:cids){
                for (String t:types){
                    CourseGrade courseGrade = new CourseGrade();
                    courseGrade.setCourseId(c);
                    courseGrade.setScoreType(t);
                    List<Grade> grades = new ArrayList<Grade>();

                    for (Student student:students){
                        String sid = student.personInfo.number;
                        List<Score> scores = student.scoreList.scores;
                        for(Score score:scores){
                            String cid = score.number;
                            String type = score.type;
                            if (cid.equals(c) && type.equals(t)){
                                Grade grade = new Grade();
                                grade.setSid(sid);
                                grade.setScore(score.point);
                                grades.add(grade);
                            }
                        }
                    }
                    Collections.sort(grades,new SortByPoint());
                    courseGrade.setGrades(grades);
                    courseGrades.add(courseGrade);
                }
            }
            courseGradeList.setCourseGrades(courseGrades);

            //生成XML
            JAXBContext jaxbContext2 = JAXBContext.newInstance(CourseGradeList.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(courseGradeList,new File("src/assignment9/resource/XML3.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}


class SortByPoint implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Grade grade1 = (Grade)o1;
        Grade grade2 = (Grade)o2;
        if (grade1.getScore() > grade2.getScore())
            return -1;
        return 1;
    }
}
