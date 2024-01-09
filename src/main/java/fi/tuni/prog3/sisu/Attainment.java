package fi.tuni.prog3.sisu;
/**
 * A class for storing students attainment
 * @author Tuomas Mäkinen
 */
public class Attainment {

    private String coursename;
    private int grade;
    private int credits;
    private String date; 

    /**
     * Constructor for attainment object
     * @param coursename name of the course
     * @param grade grade that the student got
     * @param credits credits that the student earned
     * @param date date of the course completion
     */
    public Attainment(String coursename, int grade, int credits, String date) {
        this.coursename = coursename;
        this.grade = grade;
        this.credits = credits;
        this.date = date;
    }

    /**
     * name of the completed course
     * @return name of the course as string
     */
    public String getCoursename() {
        return this.coursename;
    }
    /**
     * returns grade of the attainment
     * @return grade of the attainment as int
     */
    public int getGrade() {
        return this.grade;
    }

    /**
     * returns how many credits was earend
     * @return amount of credits as int
     */
    public int getCredits() {
        return this.credits;
    }
    
    /**
     *  returns attainment infromation as a string
     */
    @Override
    public String toString() {
        String cn = coursename;

        if (cn.length() >= 40){
            cn = coursename.substring(0, 36) + "...";
        }

        return String.format("%-40.40s  %-10s  %-10s  %-10s",
                            cn, grade, credits, date);
    }

    /**
     * return attainment date
     * @return date as a string
     */
    public String getDate() {
        return this.date;
    } 
}