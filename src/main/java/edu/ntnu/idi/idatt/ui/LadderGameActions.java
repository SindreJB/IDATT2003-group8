package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.model.GameActions;
import javafx.application.Platform;

/**
 * Implementation of the GameActions interface for the Ladder Game.
 * This class handles game flow operations like restart, new game, and exit.
 */
public class LadderGameActions implements GameActions {

  private final LadderGameBoard gameBoard;

  /**
   * Constructs a new LadderGameActionsImpl with a reference to the game board.
   * 
   * @param gameBoard The ladder game board that will be controlled
   */
  public LadderGameActions(LadderGameBoard gameBoard) {
    this.gameBoard = gameBoard;
  }

  @Override
  public void restartGame() {
    gameBoard.resetGame();
  }

  @Override
  public void startNewGame() {
    // Get the current stage from the root BorderPane
    // Stage stage = (Stage) gameBoard.getRoot().getScene().getWindow();

    // Create a new GameLauncher to return to the start page
    // GameLauncher launcher = new GameLauncher();

    // Set the start page scene on the stage
    // Scene startScene = launcher.createStartScene(stage);
    // stage.setScene(startScene);
    // stage.setTitle("Snakes and Ladders Game");

    // // If needed, adjust stage size to match the start page layout
    // stage.setWidth(600);
    // stage.setHeight(400);
    // stage.centerOnScreen();
  }

  @Override
  public void exitGame() {
    Platform.exit();
  }
}
