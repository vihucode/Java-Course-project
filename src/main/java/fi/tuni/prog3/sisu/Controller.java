package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for SisuGUI.fxml. Contains controls for
 * GUI elements, and tools for element functions.
 * @author Tuomas Mäkinen, Vili Huotari, Katariina Kariniemi
 */
public class Controller implements Initializable {
    // Test student
    private Student student;
    private static HashMap<String, Course> courses;
    private static HashMap<String, DegreeProgram> degrees = Sisu.degrees;
    private String newDegree = "NULL";
    private String newOri = "NULL";

    // Whole window:
    @FXML
    private Button quitBtn = new Button();

    // "Opiskelijan tiedot" - tab:
    @FXML
    private TextField stuPhoneField = new TextField();

    @FXML
    private TextField addressField = new TextField();

    @FXML
    private TextField stuNumField = new TextField();

    @FXML
    private TextField stuNameField = new TextField();

    @FXML
    private Button saveBtn = new Button();

    @FXML
    private TextField degreeSearchField = new TextField();

    @FXML
    private Button searchBtn = new Button();

    @FXML
    private ChoiceBox<String> degreeBox = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> orientationBox = new ChoiceBox<>();

    @FXML
    private Label resultLabel = new Label();

    @FXML
    private Label pointsLabel = new Label();

    @FXML
    private ProgressIndicator progress = new ProgressIndicator();

    @FXML
    private ListView<String> coursesDoneListView = new ListView<>();

    // "Opintojen rakenne" -tab:

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> gradeSpinner;

    @FXML
    private Label nameLabel = new Label();

    @FXML
    private Label creditLabel = new Label();

    // "Opintojen rakenne" -tab:

    /**
     * CheckBox for checking a course done
     */
    @FXML
    private CheckBox courseCheckBox = new CheckBox();

    @FXML
    private Label attainmentErrorLabel = new Label();

    @FXML
    private TreeView<String> treeView = new TreeView<>();

    /**
     * Prepares the StartUp window GUI with information required on startup.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        for (String item : degrees.keySet()) {
            degreeBox.getItems().add(item);
        }

        degreeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                orientationBox.getItems().clear();
                DegreeProgram ses = degrees.get(newValue);
                ArrayList<String> orientations = ses.getOrientations();
                if (orientations == null){
                    orientationBox.getItems().add(newValue);
                }else{
                    for ( String ori : orientations) {
                        orientationBox.getItems().add(ori);
                    }
                }
            }
        });        

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactory.setValue(1);
        gradeSpinner.setValueFactory(valueFactory);
    }

    /**
     * Quits program when quit -button is pressed, and saves all
     * new student information of current session.
     */
    @FXML
    private void quitProgram() {
        Platform.exit();
        
        SaveAndLoadStudent.saveProgress(student);
    }

    /**
     * Finds given degree in degree choicebox when search -button is pressed.
     */
    // Precondition: Given degree is found in degrees database.
    // - Postcondition: Information is displayed in degree -choicebox.
    @FXML
    private void searchDegree()
    {
        degreeBox.getItems().clear();
        orientationBox.getItems().clear();

        String searchValue = degreeSearchField.getCharacters()
                                              .toString()
                                              .toUpperCase();
        int i = 0;
        int count = 0;

        // Finds given degree in all degrees.
        for (String item : degrees.keySet()) {
            String itemToUpper = item.toUpperCase();

            i = itemToUpper.indexOf(searchValue);
            if (i != -1) {
                count =+ 1;
                degreeBox.getItems().add(item);
            }
        }

        if (count > 0){
            resultLabel.setText("- Select an item above.");
        } else {
            resultLabel.setText("- Not found! Try again.");
        }
    }

    /**
     * Gathers given information from GUI sources, writes needed information
     * into the GUI and makes required changes in student object.
     * Associated to the saveButton in GUI.
     */
    // Precondition: Information has to be selected in both fields.
    // Precondition: Cannot select same information twice.
    // - Postcondition: Given information is updated to GUI and student object.
    @FXML
    private void saveDegreeInformation(){
        String prevDegree = newDegree;
        String prevOri = newOri;
        newDegree = degreeBox.getValue();
        newOri = orientationBox.getValue();

        if (newDegree == null || newOri == null) {
            resultLabel.setText("- Fields can't be empty!");
            newDegree = "null";
            newOri = "null";
        } else if (prevDegree.equals(newDegree) && prevOri.equals(newOri)) {
            resultLabel.setText("- Degree already selected.");
        } else {
            displayInTree(newDegree);

            student.setNewDegree(newDegree);
            student.setNewOrientation(newOri);

            UpdateCreditsProgress();

            printCoursesDone();

            degreeSearchField.setText("");

            resultLabel.setText("- Information succesfully saved.");
        }

        printCoursesDone();
    }

    /**
     * Shows course completion information page when clicking on a course.
     * User can input completed courses grade and completion date
     * 
     * @param mouseEvent mouseEvent
     */
    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        datePicker.setValue(null);
        if (treeView.getSelectionModel().getSelectedItem() != null) {
            TreeItem<String> item = treeView.getSelectionModel()
                                            .getSelectedItem();
            if (courses.containsKey(item.getValue())) {
                nameLabel.setText(courses.get(item.getValue()).getName());
                creditLabel.setText(Integer.toString(courses
                                           .get(item.getValue())
                                           .getMinCredits()));
                if (!student.hasAttainment(courses.get(item.getValue())
                                                           .getName())) {
                    courseCheckBox.setSelected(false);
                } else {
                    courseCheckBox.setSelected(true);
                }
            }
        }
    }
    
    /**
     * Adds a new attainment to student if checkbox is checked by user.
     * A date must be picked otherwise the attainment wont be saved.
     * Removes attainment from student if checkbox is unchecked by user.
     * Updates progress bar, student credits and courses done in the main window
     * @param mouseEvent
     */
    // Precondition: Given date cannot be null.
    // - Postcondition: Course is updated to student and needed GUI elements.
    @FXML
    public void checkBoxClicked(MouseEvent mouseEvent) {
        if (courseCheckBox.isSelected() && !nameLabel.getText().isEmpty()) {
            String coursename = nameLabel.getText();
            int credits = Integer.parseInt(creditLabel.getText());
            
            int grade = gradeSpinner.getValue();
            if (datePicker.getValue() == null) {
                attainmentErrorLabel.setText("- Illegal date.");
                courseCheckBox.setSelected(false);
            } else {
                Attainment newAttainment = new Attainment(
                    coursename, grade, credits, datePicker.getValue()
                                                          .toString());
                
                student.addAttainment(newAttainment);
                coursesDoneListView.getItems().add(newAttainment.toString());
            }
        } else {
            student.removeAttainment(nameLabel.getText());
            printCoursesDone();
        }
        if(!nameLabel.getText().isEmpty()) {
            UpdateCreditsProgress();
        }
       
    }

    /**
     * Applies student information to GUI in its assigned fields.
     */
    public void setStudentInformation() {
        stuNameField.setText(student.getName());
        stuNumField.setText(student.getStudentNumber());

        String myDegree = student.getDegree();

        if (!myDegree.equals("NO_DEGREE")) {
            degreeBox.setValue(myDegree);
            orientationBox.setValue(student.getOrientation());
            displayInTree(myDegree);

            UpdateCreditsProgress();
        }

        String orientation = student.getOrientation();

        if (!orientation.equals("NO_ORIENTATION")){
            orientationBox.setValue(orientation);
        }

        printCoursesDone();
    }

    /**
     * Updates everything associated with student credits tracking.
     */
    private void UpdateCreditsProgress() {

        DegreeProgram degreeObj = degrees.get(student.getDegree());

        Double myCredits = (double) student.getCredits();
        Double minCredits = (double) degreeObj.getMinCredits();

        Double creditsProgress = myCredits/minCredits;
        progress.setProgress(creditsProgress);

        String points = student.getCredits() + "/" + degreeObj.getMinCredits();
        pointsLabel.setText(points);

    }

    /**
     * Sets degree information into treeView in GUI.
     * @param degree name of degree to be displayed in treeView.
     */
    private void displayInTree(String degree) {

        DegreeProgram ses = degrees.get(degree);
        ses.jsonHandler();

        courses = new HashMap<String, Course>();

        TreeItem<String> rootItem = new TreeItem<>();
        if (orientationBox.getValue() != degreeBox.getValue()){
            for (Entry<String, Module> module : ses.getModules().entrySet()){
                if (module.getKey().equals(orientationBox.getValue())){
                    rootItem = new TreeItem<>(module.getKey());
                    createTreeItems(rootItem, module.getValue().getModules());
                }
            }
        } else {
            rootItem = new TreeItem<>(degree);
            createTreeItems(rootItem, ses.getModules());
        }

        // Set tooltips for courses.
        treeView.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    TreeItem<String> treeItem = getTreeItem();
                    if (treeItem != null && treeItem.getParent() != null) {
                        Course course = courses.get(item);
                        if (course != null) {
                            String outcomes = course.getOutcomes();
                            
                            int numLines = (outcomes.length() + 50 - 1) / 50;
                            int tooltipHeight = numLines * 16;
                            
                            Tooltip tooltip = new Tooltip(outcomes);
                            tooltip.setPrefSize(255, tooltipHeight);
                            setTooltip(tooltip);
                        } else {
                            setTooltip(null);
                        }
                    }
                }
            }
        });
        treeView.setRoot(rootItem);
    }

    /**
     * Assists with setting degreeinformation to treeView in GUI.
     * Builds a TreeView item recursively.
     * @param parentItem current parent item in tree.
     * @param modules child nodes of tree.
     */
    private void createTreeItems(TreeItem<String> parentItem,
                                 HashMap<String, Module> modules) {
        for (Entry<String, Module> mod : modules.entrySet()) {

            TreeItem<String> cat = new TreeItem<>(mod.getKey());
            parentItem.getChildren().add(cat);

            if (mod.getValue().getModules().size() > 0) {
                createTreeItems(cat, mod.getValue().getModules());
            } else {
                for (Entry<String, Course> cour : mod.getValue()
                                                     .getCourses()
                                                     .entrySet()) {
                    courses.put(cour.getKey(), cour.getValue());
                    TreeItem<String> cours = new TreeItem<>(cour.getKey());

                    cat.getChildren().add(cours);
                }
            }
        }
    }

    /**
     * prints all courses done by student in a listview in main page.
     */
    public void printCoursesDone()
    {
        coursesDoneListView.getItems().clear();   
        coursesDoneListView.getItems().add(
            String.format("%-40s  %-10s  %-10s  %-10s",
            "Course name", "Grade", "Credits", "Date"));
        HashMap<String, Attainment> coursesDone = student.getCoursesDone();

           for (Attainment entry : coursesDone.values()) {
            coursesDoneListView.getItems().add(entry.toString());
           }

    }

    /**
     * Sets local variable student to given value.
     * @param myStudent given Student type value.
     */
    public void setStudent(Student myStudent) {
        this.student = myStudent;

        setStudentInformation();
    }
}
