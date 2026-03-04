package com.example.sortingvisual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Text text=new Text("Hellod");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent fxmlRoot = fxmlLoader.load();
        Scene scene = new Scene(fxmlRoot,450,450);
        scene.getStylesheets().add(HelloApplication.class.getResource("Styles.css").toExternalForm());
        Image icon=new Image("Visualisation.jpg");
        stage.getIcons().add(icon);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
