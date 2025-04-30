package edu.ntnu.idi.idatt.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
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

public class Page extends Application {
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

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
    Button loadPlayersButton = new Button("Load Players from CSV");
    Button createPlayersButton = new Button("Create new Players");
    Button settingsButton = new Button("Settings");
    Button exitButton = new Button("Exit");

    // Style the buttons
    VBox div = new VBox();
    div.getChildren().addAll(newGameButton, loadPlayersButton, createPlayersButton, settingsButton, exitButton);
    div.getStyleClass().addAll(
        "items-center", "w-200", "h-full", "mt-4", "p-4", "space-y-2");
    newGameButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    loadPlayersButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    createPlayersButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    settingsButton.getStyleClass().addAll("btn", "btn-secondary", "w-full");
    exitButton.getStyleClass().addAll("btn", "btn-destructive", "w-full");

    // Add actions to buttons
    newGameButton.setOnAction(e -> {
      LadderGameBoard gameBoard = new LadderGameBoard();
      Scene gameScene = gameBoard.createGameScene("standard", primaryStage);
      primaryStage.setScene(gameScene);
      primaryStage.setTitle("Snakes and Ladders - Standard Game");
    });
    loadGameButton.setOnAction(e -> System.out.println("Load Game clicked"));


    loadPlayersButton.setOnAction(e -> loadPlayersFromCsv());
    createPlayersButton.setOnAction(e -> edu.ntnu.idi.idatt.ui.components.CreatePlayersForm.display());
    settingsButton.setOnAction(e -> System.out.println("Settings clicked"));
    exitButton.setOnAction(e -> Platform.exit());

    // Create layout for menu
    menuLayout.setAlignment(Pos.CENTER);
    menuLayout.setPadding(new Insets(40));
    menuLayout.getChildren().addAll(titleLabel, div);
    menuLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    primaryStage.setTitle("Boardgame Menu");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Starts a new game
   */
  private void startNewGame() {
    System.out.println("Starting new game...");
    try {
      LadderGameBoard board = new LadderGameBoard();
      Stage gameStage = new Stage();
      board.start(gameStage);
      // Hide menu stage when game starts
      primaryStage.hide();

      // Show menu again when game stage closes
      gameStage.setOnCloseRequest(e -> primaryStage.show());
    } catch (Exception e) {
      showAlert("Error starting game: " + e.getMessage());
      e.printStackTrace();
    }
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
        e.printStackTrace();
      }
    }
  }

  /**
   * Opens a file chooser dialog to save sample players to a CSV file
   */
  private void savePlayersToCsv(List<Player> players) {

    String savePath = "src/main/resources/data/players.csv";
    try {
      CsvHandler.savePlayersToCsv(players, savePath);
      showAlert("Successfully saved players to players.csv");
      return;
    } catch (IOException e) {
      showAlert("Error saving players: " + e.getMessage());
      e.printStackTrace();
      return;
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