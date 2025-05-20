package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.exceptions.InitializeLadderGameException;
import edu.ntnu.idi.idatt.ui.LadderGameBoard;
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
    try {
      gameBoard.resetGame();
    } catch (InitializeLadderGameException e) {
      // Handle the exception appropriately, for example:
      System.err.println("Failed to restart game: " + e.getMessage());
      // You might want to show an error dialog to the user
    }
  }

  @Override
  public void startNewGame() {

  }

  @Override
  public void exitGame() {
    Platform.exit();
  }
}
