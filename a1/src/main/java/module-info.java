module g1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;

    opens g1 to javafx.fxml;
    exports g1;
}
