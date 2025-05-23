package edu.ntnu.idi.idatt.ui.pages;

import edu.ntnu.idi.idatt.ui.components.CreatePlayer;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainMenu extends Application {

  @Override
  public void start(Stage primaryStage) {
    Scene mainMenuScene = createMainMenuScene(primaryStage);
    primaryStage.setTitle("Boardgame Menu");
    primaryStage.setScene(mainMenuScene);
    primaryStage.show();
  }

  /**
   * Creates and returns the main menu scene
   *
   * @param primaryStage the primary stage
   * @return the created scene
   */
  public Scene createMainMenuScene(Stage primaryStage) {

    // Create background
    VBox menuLayout = new VBox(30); // Set up the scene
    Scene scene = new Scene(menuLayout);
    // Remove stylesheet reference - using inline styles only

    // Set up the main title
    Label titleLabel = new Label("Boardie");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-padding: 8;");

    // Create menu buttons
    Button newGameButton = new Button("New Game");

    Button createPlayersButton = new Button("Create new Players");
    Button exitButton = new Button("Exit");

    String baseButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand;";
    // Apply to newGameButton, loadPlayersButton, createPlayersButton
    String primaryButtonStyle = baseButtonStyle
        + "-fx-background-color: #1976d2; -fx-text-fill: white; -fx-pref-width: Infinity;";
    // Apply to exitButton
    String destructiveButtonStyle = baseButtonStyle
        + "-fx-background-color: #f44336; -fx-text-fill: white; -fx-pref-width: Infinity;"; // Style the buttons
    VBox div = new VBox();
    div.getChildren().addAll(newGameButton, /* loadPlayersButton, */ createPlayersButton, exitButton);

    // Apply inline styles to buttons
    newGameButton.setStyle(primaryButtonStyle);
    createPlayersButton.setStyle(primaryButtonStyle);
    exitButton.setStyle(destructiveButtonStyle);

    // Add hover effects
    newGameButton.setOnMouseEntered(e -> newGameButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    newGameButton.setOnMouseExited(e -> newGameButton.setStyle(primaryButtonStyle));

    createPlayersButton
        .setOnMouseEntered(e -> createPlayersButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    createPlayersButton.setOnMouseExited(e -> createPlayersButton.setStyle(primaryButtonStyle));

    exitButton.setOnMouseEntered(e -> exitButton.setStyle(destructiveButtonStyle.replace("#f44336", "#d32f2f")));
    exitButton.setOnMouseExited(e -> exitButton.setStyle(destructiveButtonStyle));

    VBox.setVgrow(exitButton, Priority.ALWAYS);

    div.setStyle(
        "-fx-alignment: center; -fx-max-width: 200px; -fx-pref-height: 100%; -fx-padding: 16; -fx-spacing: 8;");
    div.setSpacing(8);
    div.setPadding(new Insets(16));

    VBox.setMargin(div, new Insets(16, 0, 0, 0));

    // Add actions to buttons
    newGameButton.setOnAction(e -> {
      GamesMenu gamesMenu = new GamesMenu();
      Scene gamesMenuScene = gamesMenu.createGamesMenuScene(primaryStage);
      primaryStage.setScene(gamesMenuScene);
    });

    createPlayersButton.setOnAction(e -> {
      CreatePlayer.display();
    });
    exitButton.setOnAction(e -> Platform.exit());

    // Set HBox.hgrow property for buttons
    VBox.setVgrow(newGameButton, Priority.ALWAYS);

    VBox.setVgrow(createPlayersButton, Priority.ALWAYS);

    // Create layout for menu
    menuLayout.setAlignment(Pos.CENTER);
    menuLayout.getChildren().addAll(titleLabel, div);
    menuLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    return scene;
  }

  public static void main(String[] args) {
    launch(args);
  }
}