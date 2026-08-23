package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.kengjer.applytrack.model.JobApplication;

import java.io.IOException;

public class MainViewController {

    public void handleAddApplication(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("add-application-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }

    @FXML
    private VBox applicationListBox;

    @FXML
    public void initialize() {
        for (JobApplication application :
                HelloApplication.getApplicationManager().getApplications()) {

            Button applicationButton = new Button(
                    application.getId() + ". "
                            + application.getCompany() + " - "
                            + application.getPosition()
            );

            applicationButton.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader =
                            new FXMLLoader(HelloApplication.class.getResource("application-details-view.fxml"));

                    Scene scene = new Scene(fxmlLoader.load());

                    ApplicationDetailsController controller = fxmlLoader.getController();
                    controller.setApplication(application);

                    Stage stage = (Stage) applicationButton.getScene().getWindow();
                    stage.setScene(scene);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            applicationListBox.getChildren().add(applicationButton);
        }
    }
}
