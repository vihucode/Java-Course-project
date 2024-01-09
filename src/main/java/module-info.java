module fi.tuni.prog3.sisu {
    requires javafx.controls;
    requires com.google.gson;
    requires javafx.fxml;
    opens fi.tuni.prog3.sisu to com.google.gson, javafx.fxml;
    exports fi.tuni.prog3.sisu;
}
