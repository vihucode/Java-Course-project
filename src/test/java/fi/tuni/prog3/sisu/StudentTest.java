package fi.tuni.prog3.sisu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class StudentTest {
    private static Student student;
    
    @BeforeAll
    static void setUP()
    {
       student = new Student("jarmo", "12345");
       student.setNewDegree("koodari");
       student.setNewOrientation("paraskoodari");
       Attainment attainment = new Attainment("jep", 2, 4, "11-03-2022");
       Attainment attainment2 = new Attainment("matikkavol2.2323", 5, 2, "25-06-2022");
       Attainment attainment3 = new Attainment("fysiikkaa", 1, 4, "21-01-2022");
       student.addAttainment(attainment);
       student.addAttainment(attainment2);
       student.addAttainment(attainment3);
    }

    @Test
    public void studentGetterTest()
    {
       assertEquals("jarmo",student.getName());
       assertEquals("12345", student.getStudentNumber());
       assertEquals(10, student.getCredits());
       assertEquals("koodari", student.getDegree());
       assertEquals("paraskoodari", student.getOrientation());
    }

    @Test
    public void studenMethodTest()
    {   
        student.removeAttainment("fysiikkaa");
        assertEquals(2,student.getCoursesDone().size());
        assertEquals(6, student.getCredits());
        student.addAttainment(new Attainment("ohjelmointi 3 rajapinnat ja tekniikat", 5, 5, "10-5-2023"));
        assertEquals(11, student.getCredits());
        assertEquals(4, student.calculateAverageGrade());
        student.setNewOrientation("Sähkötekniikka");
        assertTrue(student.getCoursesDone().isEmpty());
        student.setNewDegree("emt");
        assertEquals("NO_ORIENTATION", student.getOrientation());
        assertEquals("emt", student.getDegree());
    }

}
