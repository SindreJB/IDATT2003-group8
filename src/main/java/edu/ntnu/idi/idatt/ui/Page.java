package edu.ntnu.idi.idatt.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Page extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Create background

    VBox menuLayout = new VBox(20);
    // Set up the scene and stage
    Scene scene = new Scene(menuLayout, 600, 400);
    scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());
    // Set up the main title
    Label titleLabel = new Label("Boardie");
    titleLabel.getStyleClass().addAll("text-2xl", "font-bold", "text-black", "p-2");

    // Create menu buttons
    Button newGameButton = new Button("New Game");
    Button loadGameButton = new Button("Load Game");
    Button settingsButton = new Button("Settings");
    Button exitButton = new Button("Exit");

    // Style the buttons

    Pane div = new Pane();
    div.getChildren().addAll(newGameButton, loadGameButton, settingsButton, exitButton);
    div.getStyleClass().addAll("flex", "flex-col", "space-y-4", "p-4", "bg-white", "rounded", "shadow");
    newGameButton.getStyleClass().addAll("btn", "btn-primary");
    loadGameButton.getStyleClass().addAll("btn", "btn-primary");
    settingsButton.getStyleClass().addAll("btn", "btn-primary");
    exitButton.getStyleClass().addAll("btn", "btn-danger");

    // Add actions to buttons
    newGameButton.setOnAction(e -> System.out.println("New Game clicked"));
    loadGameButton.setOnAction(e -> System.out.println("Load Game clicked"));
    settingsButton.setOnAction(e -> System.out.println("Settings clicked"));
    exitButton.setOnAction(e -> Platform.exit());

    // Create layout for menu
    menuLayout.setAlignment(Pos.CENTER);
    menuLayout.setPadding(new Insets(40));
    menuLayout.getChildren().addAll(titleLabel, newGameButton, loadGameButton, settingsButton, exitButton);
    menuLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    primaryStage.setTitle("Boardgame Menu");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
