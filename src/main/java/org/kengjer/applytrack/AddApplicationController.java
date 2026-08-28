package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kengjer.applytrack.model.ApplicationStatus;
import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;

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
    private Label formTitleLabel;

    @FXML
    private ComboBox<ApplicationStatus> statusComboBox;

    private JobApplication applicationToEdit;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(JobCategory.values());

        statusComboBox.getItems().setAll(ApplicationStatus.values());
        statusComboBox.setValue(ApplicationStatus.APPLIED);
        statusComboBox.setDisable(true);
    }

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(root);
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

        if (followUpDatePicker.getValue() != null
                && followUpDatePicker.getValue().isBefore(applicationDatePicker.getValue())) {
            errorLabel.setText("Follow-up date cannot be before application date.");
            return;
        }

        if (sourceField.getText() == null || sourceField.getText().isBlank()) {
            errorLabel.setText("Source is required.");
            return;
        }

        errorLabel.setText("");

        if (applicationToEdit == null) {
            JobApplication application =
                    HelloApplication.getApplicationManager().addApplication(
                            companyField.getText(),
                            positionField.getText(),
                            categoryComboBox.getValue(),
                            applicationDatePicker.getValue(),
                            sourceField.getText()
                    );

            application.setFollowUpDate(followUpDatePicker.getValue());
            application.setNotes(notesArea.getText());

        } else {
            applicationToEdit.setCompany(companyField.getText());
            applicationToEdit.setPosition(positionField.getText());
            applicationToEdit.setCategory(categoryComboBox.getValue());
            applicationToEdit.setStatus(statusComboBox.getValue());

            // Temporarily clear the old follow-up date so both dates can be updated safely.
            applicationToEdit.setFollowUpDate(null);
            applicationToEdit.setApplicationDate(applicationDatePicker.getValue());
            applicationToEdit.setFollowUpDate(followUpDatePicker.getValue());

            applicationToEdit.setSource(sourceField.getText());
            applicationToEdit.setNotes(notesArea.getText());
        }

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(root);
    }

    void setApplicationToEdit(JobApplication application) {
        this.applicationToEdit = application;

        formTitleLabel.setText("Edit Application");

        companyField.setText(application.getCompany());
        positionField.setText(application.getPosition());
        categoryComboBox.setValue(application.getCategory());

        statusComboBox.setDisable(false);
        statusComboBox.setValue(application.getStatus());

        applicationDatePicker.setValue(application.getApplicationDate());
        sourceField.setText(application.getSource());
        followUpDatePicker.setValue(application.getFollowUpDate());
        notesArea.setText(application.getNotes());
    }
}