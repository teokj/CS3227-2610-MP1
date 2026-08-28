module org.kengjer.applytrack {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.kengjer.applytrack to javafx.fxml;
    exports org.kengjer.applytrack;
}