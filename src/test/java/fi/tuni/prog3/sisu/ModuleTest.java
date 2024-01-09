package fi.tuni.prog3.sisu;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ModuleTest {
  private static Module mod;

  @BeforeAll
  static void setUp(){
    DegreeProgram degree = new DegreeProgram("Tieto- ja sähkötekniikan kandidaattiohjelma","otm-d729cfc3-97ad-467f-86b7-b6729c496c82", "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531", 5);
    degree.jsonHandler();
    mod = degree.getModule("Tietotekniikka");
  }

  // Test for Module.getModules and Module.jsonHandler
  @Test
  void testGetModules(){
    assertTrue(mod.getModules().containsKey("Tietotekniikan matemaattis-luonnontieteelliset perusopinnot"));
    assertTrue(!mod.getModules().containsKey("Tieto"));
  }

  // Test for Module.getCourses and Module.composite method
  @Test
  void testGetCourses(){
    Module mod1 = mod.getModule("Tietotekniikan matemaattis-luonnontieteelliset perusopinnot");
    assertTrue(mod1.getCourses().containsKey("Vektorianalyysi"));
    assertTrue(!mod1.getCourses().containsKey("Tieto"));
  }

}
