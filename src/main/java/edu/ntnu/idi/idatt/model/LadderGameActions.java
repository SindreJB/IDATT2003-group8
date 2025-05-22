package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.exceptions.InitializeLadderGameException;
import edu.ntnu.idi.idatt.ui.LadderGameBoardUI;
import edu.ntnu.idi.idatt.ui.pages.MainMenu;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Implementation of the GameActions interface for the Ladder Game.
 * This class handles game flow operations like restart, new game, and exit.
 */
public class LadderGameActions implements GameActions {

  private final LadderGameBoardUI gameBoard;

  /**
   * Constructs a new LadderGameActionsImpl with a reference to the game board.
   * 
   * @param gameBoard The ladder game board that will be controlled
   */
  public LadderGameActions(LadderGameBoardUI gameBoard) {
    this.gameBoard = gameBoard;
  }

  @Override
  public void restartGame() {
    try {
      gameBoard.resetGame();
    } catch (InitializeLadderGameException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Game Initialization Error");
      alert.setContentText("An error occurred while restarting the game: " + e.getMessage());
      alert.showAndWait();
    }
  }

  @Override
  public void startNewGame() {
    try {
      // Get the current stage from the game board's scene
      Stage stage = (Stage) gameBoard.getRoot().getScene().getWindow();

      // Create a new MainMenu instance
      MainMenu mainMenu = new MainMenu();

      // Create the main menu scene
      Scene mainMenuScene = mainMenu.createMainMenuScene(stage);

      // Set the main menu scene on the stage
      stage.setScene(mainMenuScene);
      stage.setTitle("Boardgame Menu");

      // Adjust stage size to match main menu
      stage.setWidth(1200);
      stage.setHeight(920);
      stage.centerOnScreen();
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Navigation Error");
      alert.setContentText("Unable to return to main menu: " + e.getMessage());
      alert.showAndWait();
    }
  }

  @Override
  public void exitGame() {
    Platform.exit();
  }
}
