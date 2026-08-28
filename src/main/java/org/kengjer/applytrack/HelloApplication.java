package org.kengjer.applytrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static final ApplicationManager applicationManager = new ApplicationManager();
    private static final Storage storage = new Storage();

    public static ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    @Override
    public void start(Stage stage) throws IOException {
        storage.loadApplications(applicationManager);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("ApplyTrack");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        storage.saveApplications(applicationManager.getApplications());
    }

    public static void main(String[] args) {
        launch();
    }
}
