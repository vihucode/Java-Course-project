package fi.tuni.prog3.sisu;

import java.util.HashMap;

/**
 * A class for storing student information
 * 
 * @author Tuomas Mäkinen
 */
public class Student {

    private String name;
    private String degree = "NO_DEGREE";
    private String orientation = "NO_ORIENTATION";
    private String studentNumber;
    private int credits = 0;
    private HashMap<String, Attainment> coursesDone = new HashMap<>();

    /**
     * Constructor for student object
     * @param name name of the student. can be anything
     * @param studentNumber students studentnumber. 
     */
    public Student(String name, String studentNumber) {
        this.name = name;
        this.studentNumber = studentNumber;
    }

    /**
     * returns name of the student
     * @return name of the student as string
     */
    public String getName() {
        return this.name;
    }

    /**
     * returns students degrees name
     * @return name of the degree as String
     */
    public String getDegree() {
        return this.degree;
    }
    
    /**
     * returns students orientation
     * @return name of the orientation as String
     */
    public String getOrientation() {
        return this.orientation;
    }

    /**
     * returns students studennumber 
     * @return studentnumber as String
     */
    public String getStudentNumber() {
        return this.studentNumber;
    }

    /**
     * returns studens total credits completed
     * @return total credits as int
     */
    public int getCredits() {
        return this.credits;
    }

    /**
     * adds attainment object into hashmap and updates students credits.
     * If an attainment with given name exists it updates the attainment
     * 
     * @param attainment attainment object to be added
     */
    public void addAttainment(Attainment attainment) {
        if (!coursesDone.containsKey(attainment.getCoursename())) {
            credits += attainment.getCredits();
        }
        coursesDone.put(attainment.getCoursename(), attainment);

    }

    /**
     * Returnds all students completed course
     * @return return HashMap of all completed courses
     */
    public HashMap<String, Attainment> getCoursesDone() {
        return this.coursesDone;
    }

    /**
     * Sets new degree for student.
     * Clears previous information about degree status
     * @param newDegree name of the new degree as string
     */
    public void setNewDegree(String newDegree) {
        this.degree = newDegree;
        coursesDone.clear();
        this.credits = 0;
        setNewOrientation("NO_ORIENTATION");
    }

    /**
     * Sets new orientation for student.
     * Clears previous information about degree status
     * @param newOrientation name of the new orientation as string
     */
    public void setNewOrientation(String newOrientation) {
        this.orientation = newOrientation;
        coursesDone.clear();
        this.credits = 0;
    }

    /**
     * Checks if student has an attainment with param name
     * 
     * @param name name of the course
     * @return true if it exist otherwise false
     */
    public boolean hasAttainment(String name) {
        return (coursesDone.containsKey(name));
    }

    /**
     * lambda function that calculates average grade and returns it
     * @return float of the average grade
     */
    public float calculateAverageGrade() {
        return (float) coursesDone.values()
                .stream()
                .mapToInt(Attainment::getGrade)
                .average()
                .orElse(0.0);
    }

    /**
     * removes course attainment from student data and updates credits
     * @param name name of the course as string 
     */
    public void removeAttainment(String name) {
        if (hasAttainment(name)) {
            credits -= coursesDone.get(name).getCredits();
            coursesDone.remove(name);
        }
    }
}
