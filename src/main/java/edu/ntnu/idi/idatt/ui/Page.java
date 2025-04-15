package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.List;

import edu.ntnu.idi.idatt.factory.BoardFactory;
import edu.ntnu.idi.idatt.model.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main menu page for the game.
 */
public class Page extends Application {
  private ComboBox<String> boardSelector;
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

    // Create board selector
    HBox boardSelectorBox = createBoardSelector();

    // Create menu buttons
    Button newGameButton = new Button("New Game");
    Button loadGameButton = new Button("Load Game");
    Button settingsButton = new Button("Settings");
    Button exitButton = new Button("Exit");

    // Style the buttons
    VBox div = new VBox();
    div.getChildren().addAll(boardSelectorBox, newGameButton, loadGameButton, settingsButton, exitButton);
    div.getStyleClass().addAll(
        "items-center", "w-200", "h-full", "mt-4", "p-4", "space-y-2");
    newGameButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    loadGameButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    settingsButton.getStyleClass().addAll("btn", "btn-secondary", "w-full");
    exitButton.getStyleClass().addAll("btn", "btn-destructive", "w-full");

    // Add actions to buttons
    newGameButton.setOnAction(e -> startNewGame());
    loadGameButton.setOnAction(e -> loadGame());
    settingsButton.setOnAction(e -> showSettings());
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
   * Creates a board selector component.
   * 
   * @return HBox containing the board selector
   */
  private HBox createBoardSelector() {
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);

    Label label = new Label("Select Board:");
    boardSelector = new ComboBox<>();

    // Populate board selector
    try {
      List<String> boards = BoardFactory.getAvailableBoards();
      if (boards.isEmpty()) {
        boardSelector.getItems().add("classic");
      } else {
        boardSelector.getItems().addAll(boards);
      }
      boardSelector.getSelectionModel().select(0);
    } catch (IOException e) {
      showErrorAlert("Error loading boards", "Could not load board configurations: " + e.getMessage());
      boardSelector.getItems().add("classic");
      boardSelector.getSelectionModel().select(0);
    }

    box.getChildren().addAll(label, boardSelector);
    return box;
  }

  /**
   * Starts a new game with the selected board.
   */
  private void startNewGame() {
    String selectedBoard = boardSelector.getValue();
    if (selectedBoard == null || selectedBoard.isEmpty()) {
      showErrorAlert("No Board Selected", "Please select a board configuration.");
      return;
    }

    try {
      // Load the selected board
      Board board = BoardFactory.createBoard(selectedBoard);

      // Start the game board UI
      LadderGameBoard gameBoard = new LadderGameBoard();
      gameBoard.start(new Stage());

      // Close the menu
      primaryStage.close();
    } catch (Exception e) {
      showErrorAlert("Error Starting Game", "Could not start the game: " + e.getMessage());
    }
  }

  /**
   * Loads a saved game.
   */
  private void loadGame() {
    // This would load a saved game state
    showInfoAlert("Not Implemented", "Loading saved games is not yet implemented.");
  }

  /**
   * Shows the settings screen.
   */
  private void showSettings() {
    // This would show game settings
    showInfoAlert("Not Implemented", "Settings screen is not yet implemented.");
  }

  /**
   * Shows an error alert.
   * 
   * @param title   The title of the alert
   * @param message The message to display
   */
  private void showErrorAlert(String title, String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Shows an information alert.
   * 
   * @param title   The title of the alert
   * @param message The message to display
   */
  private void showInfoAlert(String title, String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}