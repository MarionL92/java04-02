package com.monprojet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        primaryStage.setTitle("Gestion des Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}