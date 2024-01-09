package fi.tuni.prog3.sisu;

/**
 * 
 * The Course class represents a single course within a degree program.
 * It extends the DegreeModule class and inherits its attributes and methods.
 * @author Vili Huotari
 */
public class Course extends DegreeModule {

  private String outcomes;
  /**
   * Constructor for course object
   * @param name name of the course as string
   * @param id localID of the course in Sisu API
   * @param groupId groupID of the course in Sisu API 
   * @param minCredits minumum credits that can be earned from the course
   */
  public Course(String name, 
                String id, 
                String groupId, 
                int minCredits, 
                String outcomes) {
    super(name, id, groupId, minCredits);
    this.outcomes = outcomes;
  }

  /**
   * Add rows to string and return outcomes of course
   * @return String outcomes
   */
  public String getOutcomes() {
    String modifiedString = new String();
    for (int i = 0; i < outcomes.length(); i++) {
      char c = outcomes.charAt(i);
      modifiedString += c;
      if ((i + 1) % 50 == 0) {
          modifiedString += "\n";
      }
  }
    return modifiedString;
  }
}
