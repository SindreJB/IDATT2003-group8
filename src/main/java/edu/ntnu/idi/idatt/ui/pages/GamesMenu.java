package edu.ntnu.idi.idatt.ui.pages;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GamesMenu {

    private BorderPane root;

    public Scene createGamesMenuScene(Stage primaryStage) {

        root = new BorderPane();
        root.setStyle("-fx-background-color: #F0EFEB;");

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}
