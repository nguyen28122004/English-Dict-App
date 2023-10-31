package com.harafx;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("English Vocabulary Apps");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

}