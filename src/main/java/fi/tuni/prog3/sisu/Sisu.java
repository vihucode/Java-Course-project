package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Sisu
 * @author Katariina Kariniemi, Vili Huotari
 */
public class Sisu extends Application {
    private SisuAPI session;
    public static HashMap<String, DegreeProgram> degrees = new HashMap<>();
    Controller main = new Controller();

    /**
     * Constructor for Sisu GUI
     */
    public Sisu() {
        this.session = new SisuAPI(1, "");
        JsonObject degreesObj = session.getJsonObjectFromApi();
        jsonHandler(degreesObj);
    }

    /**
     * Runs GUI.
     * @param stage main stage.
     */
    @Override
    public void start(Stage stage) throws IOException {

        // Creating Parent objects to all used windows: 

        FXMLLoader mainLoader = new FXMLLoader(
            this.getClass().getResource("/SisuGUI.fxml"));
        Parent mainParent = mainLoader.load();

        FXMLLoader startupLoader = new FXMLLoader(
            Sisu.class.getResource("/StartupGUI.fxml"));
        Parent startupParent = startupLoader.load();

        FXMLLoader registerLoader = new FXMLLoader(
            this.getClass().getResource("/RegisterGUI.fxml"));
        Parent registerParent = registerLoader.load();

        // Setting required information to all controllers:

        Controller mainController = mainLoader.getController();

        StartupController setUpController = startupLoader.getController();
        setUpController.setRegisterParent(registerParent);
        setUpController.setMainParent(mainParent);
        setUpController.setMainController(mainController);
        setUpController.setStage(stage);

        RegisterController regController = registerLoader.getController();
        regController.setStartupParent(startupParent);
        regController.setStage(stage);

        Scene startupScene = new Scene(startupParent, 504, 315);
        startupScene.getStylesheets().add(
            this.getClass().getResource("Sisu.css").toExternalForm());
        stage.setScene(startupScene);
        
        stage.setTitle("SISU");
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Calls the launch method to launch GUI.
     * @param args unused.
     * @throws RuntimeException
     */
    public static void main(String[] args) throws RuntimeException {
        launch();
    }

    /**
     * 
     * Handles JSON object returned from Sisu API search request and stores
     * retrieved degree programs into a HashMap.
     * 
     * @param obj The JSON object returned from the Sisu API search request.
     */
    public void jsonHandler(JsonObject obj){
        JsonArray arr = obj.getAsJsonArray("searchResults");
        Iterator<JsonElement> iter = arr.iterator();
        while (iter.hasNext()) {
            JsonObject jsonObj = iter.next().getAsJsonObject();
            JsonElement id = jsonObj.get("id");
            JsonElement groupId = jsonObj.get("groupId");
            JsonElement name = jsonObj.get("name");
            JsonElement minCredit = 
            jsonObj.get("credits").getAsJsonObject().get("min");

            DegreeProgram deg = 
            new DegreeProgram(name.getAsString(),
                              id.getAsString(),
                              groupId.getAsString(),
                              minCredit.getAsInt());
            degrees.put(name.getAsString(), deg);
        }
    }
    /**
     * Returns all degrees fetched from Sisu API
     * @return HashMap of the degrees
     */
    public static HashMap<String, DegreeProgram> getDegrees() {
        return degrees;
    }
}