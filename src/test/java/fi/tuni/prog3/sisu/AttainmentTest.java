package fi.tuni.prog3.sisu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AttainmentTest {
    
    @Test 
    public void attainmentTest(){

        Attainment test = new Attainment("FYS.101", 2, 5, "21-11-2022");
        assertEquals("FYS.101", test.getCoursename());
        assertEquals(5, test.getCredits());
        assertEquals(2, test.getGrade());
        assertEquals("21-11-2022", test.getDate());
    }
}
