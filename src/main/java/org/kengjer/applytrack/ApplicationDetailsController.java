package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.kengjer.applytrack.model.FollowUpStatus;
import org.kengjer.applytrack.model.JobApplication;

import java.io.IOException;
import java.time.LocalDate;

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

    @FXML
    private Button starButton;

    @FXML
    private Label starredLabel;

    @FXML
    private Label followUpStatusLabel;

    private JobApplication application;

    void setApplication(JobApplication application) {
        this.application = application;

        if (application.isStarred()) {
            starredLabel.setText("★ Starred");
            starButton.setText("Unstar");
        } else {
            starredLabel.setText("☆ Not Starred");
            starButton.setText("Star");
        }

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

        FollowUpStatus followUpStatus =
                application.getFollowUpStatus(LocalDate.now());
        switch (followUpStatus) {
            case NONE:
                followUpStatusLabel.setText("Follow-up Status: None");
                break;
            case OVERDUE:
                followUpStatusLabel.setText("Follow-up Status: OVERDUE");
                break;
            case DUE_TODAY:
                followUpStatusLabel.setText("Follow-up Status: DUE TODAY");
                break;
            case UPCOMING:
                followUpStatusLabel.setText("Follow-up Status: UPCOMING");
                break;
            case FUTURE:
                followUpStatusLabel.setText("Follow-up Status: FUTURE");
                break;
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

        Parent root = fxmlLoader.load();

        AddApplicationController controller = fxmlLoader.getController();
        controller.setApplicationToEdit(application);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(root);
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

                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                            .getScene()
                            .getWindow();

                    stage.getScene().setRoot(root);

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

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(root);
    }

    @FXML
    public void handleStar() {
        application.setStarred(!application.isStarred());

        if (application.isStarred()) {
            starButton.setText("Unstar");
            starredLabel.setText("★ Starred");
        } else {
            starButton.setText("Star");
            starredLabel.setText("☆ Not Starred");
        }
    }
}