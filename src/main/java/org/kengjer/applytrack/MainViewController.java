package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import org.kengjer.applytrack.model.ApplicationStatus;
import org.kengjer.applytrack.model.JobCategory;
import org.kengjer.applytrack.model.JobApplication;

import java.io.IOException;

public class MainViewController {

    public void handleAddApplication(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("add-application-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 650);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }

    @FXML
    private VBox applicationListBox;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    @FXML
    private ComboBox<String> categoryFilterComboBox;

    @FXML
    private CheckBox starredFilterCheckBox;

    @FXML
    public void initialize() {
        statusFilterComboBox.getItems().add("All statuses");
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusFilterComboBox.getItems().add(status.name());
        }

        categoryFilterComboBox.getItems().add("All categories");
        for (JobCategory category : JobCategory.values()) {
            categoryFilterComboBox.getItems().add(category.name());
        }

        ApplicationManager manager = HelloApplication.getApplicationManager();

        statusFilterComboBox.setValue(manager.getSelectedStatusFilter());
        categoryFilterComboBox.setValue(manager.getSelectedCategoryFilter());

        starredFilterCheckBox.setSelected(manager.isStarredOnlyFilter());

        statusFilterComboBox.setOnAction(event -> {
            manager.setSelectedStatusFilter(statusFilterComboBox.getValue());
            displayApplications();
        });

        categoryFilterComboBox.setOnAction(event -> {
            manager.setSelectedCategoryFilter(categoryFilterComboBox.getValue());
            displayApplications();
        });

        starredFilterCheckBox.setOnAction(event -> {
            manager.setStarredOnlyFilter(starredFilterCheckBox.isSelected());
            displayApplications();
        });

        displayApplications();
    }

    private void displayApplications() {
        applicationListBox.getChildren().clear();

        String selectedStatus = statusFilterComboBox.getValue();
        String selectedCategory = categoryFilterComboBox.getValue();
        boolean starredOnly = starredFilterCheckBox.isSelected();

        for (JobApplication application :
                HelloApplication.getApplicationManager().getApplications()) {

            if (!selectedStatus.equals("All statuses")
                    && application.getStatus() != ApplicationStatus.valueOf(selectedStatus)) {
                continue;
            }

            if (!selectedCategory.equals("All categories")
                    && application.getCategory() != JobCategory.valueOf(selectedCategory)) {
                continue;
            }

            if (starredOnly && !application.isStarred()) {
                continue;
            }

            String starPrefix = application.isStarred() ? "★ " : "";

            Button applicationButton = new Button(
                    starPrefix
                            + application.getId() + ". "
                            + application.getCompany() + " - "
                            + application.getPosition()
            );

            applicationButton.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader =
                            new FXMLLoader(HelloApplication.class.getResource(
                                    "application-details-view.fxml"));

                    Scene scene = new Scene(fxmlLoader.load());

                    ApplicationDetailsController controller =
                            fxmlLoader.getController();
                    controller.setApplication(application);

                    Stage stage =
                            (Stage) applicationButton.getScene().getWindow();

                    stage.setScene(scene);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            applicationListBox.getChildren().add(applicationButton);
        }
    }

    @FXML
    public void handleResetFilters() {
        statusFilterComboBox.setValue("All statuses");
        categoryFilterComboBox.setValue("All categories");
        starredFilterCheckBox.setSelected(false);

        HelloApplication.getApplicationManager()
                .setStarredOnlyFilter(false);

        displayApplications();
    }
}
