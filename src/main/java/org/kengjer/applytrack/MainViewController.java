package org.kengjer.applytrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kengjer.applytrack.model.ApplicationStatus;
import org.kengjer.applytrack.model.FollowUpStatus;
import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;

import java.io.IOException;
import java.time.LocalDate;

public class MainViewController {

    @FXML
    public void handleAddApplication(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("add-application-view.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(root);
    }

    @FXML
    private VBox applicationListBox;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    @FXML
    private ComboBox<String> categoryFilterComboBox;

    @FXML
    private ComboBox<String> followUpFilterComboBox;

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

        followUpFilterComboBox.getItems().add("All follow-ups");
        for (FollowUpStatus status : FollowUpStatus.values()) {
            followUpFilterComboBox.getItems().add(
                    status.name().replace("_", " ")
            );
        }

        ApplicationManager manager = HelloApplication.getApplicationManager();

        statusFilterComboBox.setValue(manager.getSelectedStatusFilter());
        categoryFilterComboBox.setValue(manager.getSelectedCategoryFilter());
        followUpFilterComboBox.setValue(manager.getSelectedFollowUpFilter());

        starredFilterCheckBox.setSelected(manager.isStarredOnlyFilter());

        statusFilterComboBox.setOnAction(event -> {
            manager.setSelectedStatusFilter(statusFilterComboBox.getValue());
            displayApplications();
        });

        categoryFilterComboBox.setOnAction(event -> {
            manager.setSelectedCategoryFilter(categoryFilterComboBox.getValue());
            displayApplications();
        });

        followUpFilterComboBox.setOnAction(event -> {
            manager.setSelectedFollowUpFilter(followUpFilterComboBox.getValue());
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
        String selectedFollowUp = followUpFilterComboBox.getValue();
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

            FollowUpStatus followUpStatus =
                    application.getFollowUpStatus(LocalDate.now());
            if (!selectedFollowUp.equals("All follow-ups")
                    && followUpStatus != FollowUpStatus.valueOf(
                    selectedFollowUp.replace(" ", "_"))) {
                continue;
            }

            String followUpSuffix = "";

            switch (followUpStatus) {
                case OVERDUE:
                    followUpSuffix = " [⚠ OVERDUE]";
                    break;
                case DUE_TODAY:
                    followUpSuffix = " [DUE TODAY]";
                    break;
                case UPCOMING:
                    followUpSuffix = " [UPCOMING]";
                    break;
                default:
                    break;
            }

            String starPrefix = application.isStarred() ? "★ " : "";

            Button applicationButton = new Button(
                    starPrefix
                            + application.getId() + ". "
                            + application.getCompany() + " - "
                            + application.getPosition()
                            + followUpSuffix
            );

            applicationButton.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader =
                            new FXMLLoader(HelloApplication.class.getResource(
                                    "application-details-view.fxml"));

                    Parent root = fxmlLoader.load();

                    ApplicationDetailsController controller =
                            fxmlLoader.getController();
                    controller.setApplication(application);

                    Stage stage =
                            (Stage) applicationButton.getScene().getWindow();

                    stage.getScene().setRoot(root);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            applicationButton.setMaxWidth(Double.MAX_VALUE);
            applicationListBox.getChildren().add(applicationButton);
        }
    }

    @FXML
    public void handleResetFilters() {
        statusFilterComboBox.setValue("All statuses");
        categoryFilterComboBox.setValue("All categories");
        followUpFilterComboBox.setValue("All follow-ups");
        starredFilterCheckBox.setSelected(false);

        HelloApplication.getApplicationManager()
                .setStarredOnlyFilter(false);

        displayApplications();
    }
}
