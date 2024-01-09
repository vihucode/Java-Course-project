package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for RegisterGUI.fxml. Contains controls for
 * GUI elements, and tools for element functions.
 * @author Katariina kariniemi
 */
public class RegisterController implements Initializable {
    private Stage stage = new Stage();
    private Parent startupParent = new Parent() {};

    @FXML
    Button registerButton = new Button();

    @FXML
    TextField nameField = new TextField();

    @FXML
    TextField stuNumField = new TextField();

    @FXML
    Button backButton = new Button();

    @FXML
    Label resultLabel = new Label();

    /**
     * Initializes needed information in registration window.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        resultLabel.setText("- Type name and student number.");
        nameField.setText("");
        stuNumField.setText("");
    }

    /**
     * Opens Login -view when backButton is pressed.
     */
    @FXML
    public void goBack()
    {
        stage.setScene(startupParent.getScene());
        initialize(null,null);
    }

    /**
     * Saves given student information to a json file.
     */
    // Precondition: Given name and number field can't be empty.
    // Precondition: Student cannot already exist in database
    //               (Student number is already registered).
    // Precondition: Given name and number can't contain numbers.
    // Precondition: Student number must be under 15 characters.
    // - Postcondition: Student information is written to database.
    @FXML
    public void registerUser()
    {
        String name = nameField.getText();
        String number = stuNumField.getText();
        Student newStudent = new Student(name, number);

        if (number.isEmpty() || name.isEmpty()) {
            resultLabel.setText("- Please fill out both fields.");
        } else if (SaveAndLoadStudent.studentExists(number)){
            resultLabel.setText("- Student already exists!");
        } else if (!nameField.getText().matches("[a-zA-Z ]+")){
            resultLabel.setText("- Name can not include numbers!");
        } else if (number.length() >= 15){
            resultLabel.setText("- Student number too long!");
        } else {
            SaveAndLoadStudent.saveProgress(newStudent);
            resultLabel.setText(
            "- Registering was a success! "
            + "Press \"back\" to return to login view.");
        }
    }

    /**
     * Sets local startupParent variable to given value.
     * @param myParent the given Parent type value.
     */
    public void setStartupParent(Parent myParent)
    {
        startupParent = myParent;
    }

    /**
     * Sets local stage variable to given value.
     * @param myStage the given Stage type value.
     */
    public void setStage(Stage myStage)
    {
        stage = myStage;
    }
}