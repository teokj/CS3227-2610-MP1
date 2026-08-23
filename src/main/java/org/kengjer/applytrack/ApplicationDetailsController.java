package org.kengjer.applytrack;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.kengjer.applytrack.model.JobApplication;

public class ApplicationDetailsController {

    @FXML
    private Label companyLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label applicationDateLabel;

    @FXML
    private Label sourceLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label followUpDateLabel;

    @FXML
    private Label notesLabel;

    private JobApplication application;

    void setApplication(JobApplication application) {
        this.application = application;

        companyLabel.setText("Company: " + application.getCompany());
        positionLabel.setText("Position: " + application.getPosition());
        categoryLabel.setText("Category: " + application.getCategory());
        applicationDateLabel.setText("Application Date: " + application.getApplicationDate());
        sourceLabel.setText("Source: " + application.getSource());
        statusLabel.setText("Status: " + application.getStatus());

        if (application.getFollowUpDate() == null) {
            followUpDateLabel.setText("Follow-up Date: None");
        } else {
            followUpDateLabel.setText("Follow-up Date: " + application.getFollowUpDate());
        }

        if (application.getNotes().isBlank()) {
            notesLabel.setText("Notes: None");
        } else {
            notesLabel.setText("Notes: " + application.getNotes());
        }
    }

    @FXML
    public void handleEdit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("add-application-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        AddApplicationController controller = fxmlLoader.getController();
        controller.setApplicationToEdit(application);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }

    @FXML
    public void handleDelete(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete this application?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirmation.setHeaderText("Confirm Delete");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                HelloApplication.getApplicationManager().removeApplication(application);

                try {
                    FXMLLoader fxmlLoader =
                            new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

                    Scene scene = new Scene(fxmlLoader.load(), 320, 240);

                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                            .getScene()
                            .getWindow();

                    stage.setScene(scene);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }
}