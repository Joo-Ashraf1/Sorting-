package com.example.sortingvisual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent fxmlRoot = fxmlLoader.load();
        Scene scene = new Scene(fxmlRoot,450,450);
        // scene.getStylesheets().add(HelloApplication.class.getResource("Styles.css").toExternalForm());
        Image icon=new Image("Visualisation.jpg");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Sorting Visualizer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
