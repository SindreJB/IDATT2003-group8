package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.model.GameActions;
import javafx.application.Platform;

/**
 * Implementation of the GameActions interface for the Ladder Game.
 * This class handles game flow operations like restart, new game, and exit.
 */
public class LadderGameActionsImpl implements GameActions {

  private final LadderGameBoard gameBoard;

  /**
   * Constructs a new LadderGameActionsImpl with a reference to the game board.
   * 
   * @param gameBoard The ladder game board that will be controlled
   */
  public LadderGameActionsImpl(LadderGameBoard gameBoard) {
    this.gameBoard = gameBoard;
  }

  @Override
  public void restartGame() {
    gameBoard.resetGame();
  }

  @Override
  public void startNewGame() {
    gameBoard.setupNewGame();
  }

  @Override
  public void exitGame() {
    Platform.exit();
  }
}
