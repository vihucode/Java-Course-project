package fi.tuni.prog3.sisu;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DegreeProgramTest {

  // Test for degreeProgrrm.getModules, dergeeProgram.jsonHandler and degreeProgram.composite
  @Test
  public void testGetModules() throws Exception {
    DegreeProgram degree = new DegreeProgram("Tieto- ja sähkötekniikan kandidaattiohjelma","otm-d729cfc3-97ad-467f-86b7-b6729c496c82", "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531", 5);
    degree.jsonHandler();
    assertTrue(degree.getModules().containsKey("Tietotekniikka"));
    assertTrue(!degree.getModules().containsKey("Tieto"));
  }

}

