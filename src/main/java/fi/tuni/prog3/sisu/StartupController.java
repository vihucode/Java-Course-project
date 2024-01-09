package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for StartupGUI.fxml. Contains controls for
 * GUI elements, and tools for element functions.
 * @author Katariina Kariniemi
 */
public class StartupController implements Initializable {

    private Stage stage = new Stage();
    private Parent registerParent = new Parent(){};
    private Parent mainParent = new Parent(){};
    private Controller mainController = new Controller();

    @FXML
    private TextField stuNumField = new TextField();

    @FXML
    private Button loginButton = new Button();

    @FXML
    private Label resultLabel = new Label();

    @FXML
    private Button registerButton = new Button();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultLabel.setText("- Start by logging in or registering a new user.");
        stuNumField.setText("");
    }

    /**
     * Opens main window and processes login information
     * when Login button is pressed in GUI.
     */
    // Precondition: Given student number has to exist in database.
    // - Postcondition: Given student's information is read from database
    //              and main window containing student's information opens up.
    @FXML
    public void openMainWindow() {

        String studentNumber = stuNumField.getText();
        Student student = SaveAndLoadStudent.loadProgress(studentNumber);

        if (student == null){
            resultLabel.setText("- Not found! Try again.");
        } else {
            Scene mainScene = new Scene(mainParent, 812, 615);
            mainScene.getStylesheets().add(
                this.getClass().getResource("Sisu.css").toExternalForm());

            Student newStudent = SaveAndLoadStudent.loadProgress(studentNumber);
            mainController.setStudent(newStudent);

            stage.setScene(mainScene);
        }
    }

    /**
     * Opens registration window when Register -button is pressed.
     */
    @FXML
    public void openRegister(){
        Scene registerScene = registerParent.getScene();

        if (registerScene == null) {
            registerScene = new Scene(registerParent, 504, 315);
            registerScene.getStylesheets().add(
                this.getClass().getResource("Sisu.css").toExternalForm());
            stage.setScene(registerScene);
        } else {
            stage.setScene(registerScene);
        }

        initialize(null, null);
    }

    /**
     * Sets local value registerParent to the given value.
     * @param myParent given Param type parameter.
     */
    public void setRegisterParent(Parent myParent) {
        registerParent = myParent;
    }

    /**
     * Sets local value mainParent to given value.
     * @param myParent given Parent type parameter.
     */
    public void setMainParent(Parent myParent) {
        mainParent = myParent;
    }

    /**
     * Sets loval value mainparent to given value.
     * @param myController given Parent type parameter.
     */
    public void setMainController(Controller myController) {
        mainController = myController;
    }

    /**
     * Sets local value stage to given value.
     * @param newStage given Stage type parameter.
     */
    public void setStage(Stage newStage){
        stage = newStage;
    }
}
