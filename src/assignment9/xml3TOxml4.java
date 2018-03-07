package assignment9;

import assignment9.model.CourseGrade;
import assignment9.model.CourseGradeList;
import assignment9.model.Grade;
import assignment9.model.StudentList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ariana on 2018/3/7.
 */
public class xml3TOxml4 {
    public static void main(String[] args) {
        File xml3=new File("src/assignment9/resource/XML文档4.xml");
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(CourseGradeList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CourseGradeList courseGradeList = (CourseGradeList)unmarshaller.unmarshal(xml3);
            List<CourseGrade> list=courseGradeList.getCourseGrades();
            CourseGradeList newCoureseGradeList = new CourseGradeList();
            List<CourseGrade> newList=new ArrayList<>();
            int countAll=0;
            int countNot60=0;
            for(CourseGrade courseGrade:list){
                countAll++;
                List<Grade> grades=courseGrade.getGrades();
                for(Grade grade:grades){
                    if(grade.getScore()<60){
                        newList.add(courseGrade);
                        System.out.println("break");
                        countNot60++;
                        break;
                    }
                }
            }
            System.out.println("所有："+countAll+" 含有不及格的："+countNot60);

            newCoureseGradeList.setCourseGrades(newList);
            JAXBContext jaxbContext2 = JAXBContext.newInstance(CourseGradeList.class);
            Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(newCoureseGradeList,new File("src/assignment9/resource/XML4.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
