package edu.ntnu.idi.idatt.ui.pages;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
import edu.ntnu.idi.idatt.ui.components.CreatePlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainMenu extends Application {
  private Stage primaryStage;

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
    this.primaryStage = primaryStage;

    // Create background
    VBox menuLayout = new VBox(30);

    // Set up the scene
    Scene scene = new Scene(menuLayout);
    scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

    // Set up the main title
    Label titleLabel = new Label("Boardie");
    titleLabel.getStyleClass().addAll("text-2xl", "font-bold", "text-black", "p-2");

    // Create menu buttons
    Button newGameButton = new Button("New Game");
    Button loadPlayersButton = new Button("Load Players from CSV");
    Button createPlayersButton = new Button("Create new Players");
    Button exitButton = new Button("Exit");

    // Style the buttons
    VBox div = new VBox();
    div.getChildren().addAll(newGameButton, loadPlayersButton, createPlayersButton, exitButton);
    div.getStyleClass().addAll(
        "items-center", "w-200", "h-full", "mt-4", "p-4", "space-y-2");
    newGameButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    loadPlayersButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    createPlayersButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    exitButton.getStyleClass().addAll("btn", "btn-destructive", "w-full");

    // Add actions to buttons
    newGameButton.setOnAction(e -> {
      GamesMenu gamesMenu = new GamesMenu();
      Scene gamesMenuScene = gamesMenu.createGamesMenuScene(primaryStage);
      primaryStage.setScene(gamesMenuScene);
    });

    loadPlayersButton.setOnAction(e -> loadPlayersFromCsv());
    createPlayersButton.setOnAction(e -> {
      CreatePlayer.display();
    });
    exitButton.setOnAction(e -> Platform.exit());

    // Create layout for menu
    menuLayout.setAlignment(Pos.CENTER);
    menuLayout.getChildren().addAll(titleLabel, div);
    menuLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    return scene;
  }

  /**
   * Opens a file chooser dialog to load players from a CSV file
   */
  private void loadPlayersFromCsv() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Players");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

    File selectedFile = fileChooser.showOpenDialog(primaryStage);
    if (selectedFile != null) {
      try {
        List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv(selectedFile.getPath());

        if (loadedPlayers.isEmpty()) {
          showAlert("No players found in the file.");
        } else {
          showAlert("Successfully loaded " + loadedPlayers.size() + " players.");
        }
      } catch (IOException e) {
        showAlert("Error loading players: " + e.getMessage());
      }
    }
  }

  /**
   * Shows an information alert dialog
   * 
   * @param message the message to show
   */
  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}