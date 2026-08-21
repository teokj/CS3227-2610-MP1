package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.kengjer.applytrack.model.JobCategory;
import org.kengjer.applytrack.model.JobApplication;

import java.io.IOException;

public class AddApplicationController {

    @FXML
    private TextField companyField;

    @FXML
    private TextField positionField;

    @FXML
    private ComboBox<JobCategory> categoryComboBox;

    @FXML
    private DatePicker applicationDatePicker;

    @FXML
    private TextField sourceField;

    @FXML
    private DatePicker followUpDatePicker;

    @FXML
    private TextArea notesArea;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(JobCategory.values());
    }

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }

    @FXML
    public void handleSave(ActionEvent event) throws IOException {
        if (companyField.getText() == null || companyField.getText().isBlank()) {
            errorLabel.setText("Company is required.");
            return;
        }

        if (positionField.getText() == null || positionField.getText().isBlank()) {
            errorLabel.setText("Position is required.");
            return;
        }

        if (categoryComboBox.getValue() == null) {
            errorLabel.setText("Category is required.");
            return;
        }

        if (applicationDatePicker.getValue() == null) {
            errorLabel.setText("Application date is required.");
            return;
        }

        if (sourceField.getText() == null || sourceField.getText().isBlank()) {
            errorLabel.setText("Source is required.");
            return;
        }

        errorLabel.setText("");

        JobApplication application = new JobApplication(
                1,
                companyField.getText(),
                positionField.getText(),
                categoryComboBox.getValue(),
                applicationDatePicker.getValue(),
                sourceField.getText()
        );

        application.setFollowUpDate(followUpDatePicker.getValue());
        application.setNotes(notesArea.getText());

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }
}