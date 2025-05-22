package edu.ntnu.idi.idatt.controller;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.factory.LadderGameFactory;
import edu.ntnu.idi.idatt.model.LadderBoard;
import edu.ntnu.idi.idatt.model.LadderGameTile;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.observer.GameEvent;

/**
 * LadderGameController extends the abstract GameController class
 * with specific logic for Snakes and Ladders game.
 */
public class LadderGameController extends GameController {

  // Event listeners/callbacks for UI updates
  private Runnable onGameWon;
  private RollResultCallback onDiceRolled;
  private PlayerMoveCallback onPlayerMovement;

  /**
   * Interface for the dice roll result callback
   */
  public interface RollResultCallback {
    void onDiceRolled(int diceValue, String message, int oldPosition, int newPosition);
  }

  /**
   * Interface for detailed player movement callback
   */
  public interface PlayerMoveCallback {
    void onPlayerMovement(Player player, int oldPosition, int newPosition, boolean checkVictory);
  }

  /**
   * Creates a new LadderGameController with a default board
   */
  public LadderGameController() {
    super();
    loadBoard(new LadderBoard(10, 9));
  }

  /**
   * Loads a specific type of ladder board
   * 
   * @param boardType The type of board to load
   * @return True if loading was successful
   * @throws FileWriteException If there is an error writing to a file
   */
  public boolean loadBoard(String boardType) throws FileWriteException {
    Optional<LadderBoard> loadedBoard = LadderGameFactory.tryCreateBoard(boardType);
    if (loadedBoard.isPresent()) {
      loadBoard(loadedBoard.get());
      return true;
    }
    return false;
  }

  /**
   * Returns the game board as a LadderBoard
   * 
   * @return The LadderBoard
   */
  public LadderBoard getLadderBoard() {
    return (LadderBoard) getGameBoard();
  }

  /**
   * Performs dice rolling and player movement logic
   * 
   * @return The dice roll value
   */
  public int rollDiceAndMove() {
    // Roll the dice
    int diceValue = rollDice();

    // Get player's current position
    Player currentPlayer = getCurrentPlayer();
    int oldPosition = currentPlayer.getTileId();

    // Calculate new position within board limits
    LadderBoard ladderBoard = getLadderBoard();
    int newPosition = Math.min(oldPosition + diceValue,
        ladderBoard.getRows() * ladderBoard.getColumns());

    // Store landed position before processing special tiles
    int landedPosition = newPosition;

    // Process tile actions (handles snakes, ladders, wormholes)
    newPosition = processTileActions(currentPlayer, newPosition);

    // Create movement message based on what happened
    String message = createMovementMessage(currentPlayer, diceValue, oldPosition, landedPosition, newPosition);

    // Update player position
    movePlayer(currentPlayer, oldPosition, newPosition);

    // Notify listeners via callbacks (legacy)
    if (onDiceRolled != null) {
      onDiceRolled.onDiceRolled(diceValue, message, oldPosition, newPosition);
    }

    // Check for victory
    boolean hasWon = checkVictory(currentPlayer);

    // Notify about player movement (for UI animation)
    if (onPlayerMovement != null) {
      onPlayerMovement.onPlayerMovement(currentPlayer, oldPosition, newPosition, hasWon);
    }

    if (hasWon) {
      // Legacy notification
      if (onGameWon != null) {
        onGameWon.run();
      }
      return diceValue;
    }

    // Switch to next player
    switchToNextPlayer();

    return diceValue;
  }

  /**
   * Creates a descriptive message about the player's movement
   */
  private String createMovementMessage(Player player, int diceValue, int oldPosition, int landedPosition,
      int newPosition) {
    LadderBoard board = getLadderBoard();
    LadderGameTile landedTile = board.getTile(landedPosition);

    if (landedTile.hasLadder()) {
      return player.getName() + " rolled " + diceValue +
          " and climbed a ladder from " + landedPosition + " to " + newPosition;
    } else if (landedTile.hasSnake()) {
      return player.getName() + " rolled " + diceValue +
          " and slid down a snake from " + landedPosition + " to " + newPosition;
    } else if (landedTile.hasWormhole()) {
      int movement = newPosition - landedPosition;
      if (movement > 0) {
        return player.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you forward " + movement +
            " spaces to " + newPosition + "!";
      } else if (movement < 0) {
        return player.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you backward " + Math.abs(movement) +
            " spaces to " + newPosition + "!";
      } else {
        return player.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole spun you around but left you in the same place!";
      }
    } else {
      return player.getName() + " rolled " + diceValue +
          " and moved from " + oldPosition + " to " + newPosition;
    }
  }

  /**
   * Process special tile actions
   * 
   * @param player The player who landed on the tile
   * @param tileId The tile ID to process
   * @return The new position after processing tile actions
   */
  @Override
  public int processTileActions(Player player, int tileId) {
    LadderBoard board = getLadderBoard();
    LadderGameTile tile = board.getTile(tileId);

    if (tile.hasLadder()) {
      int newPosition = tile.getLadder().getNumber();
      notifyObservers(new GameEvent("LADDER_CLIMBED",
          Map.of("player", player, "from", tileId, "to", newPosition)));
      return newPosition;
    } else if (tile.hasSnake()) {
      int newPosition = tile.getSnake().getNumber();
      notifyObservers(new GameEvent("SNAKE_SLIDE",
          Map.of("player", player, "from", tileId, "to", newPosition)));
      return newPosition;
    } else if (tile.hasWormhole()) {
      // For wormholes, generate a random movement between -15 and +20 tiles
      int randomMovement = new Random().nextInt(36) - 15;
      int newPosition = Math.max(1, tileId + randomMovement);

      notifyObservers(new GameEvent("WORMHOLE_TELEPORT",
          Map.of("player", player, "from", tileId, "to", newPosition, "movement", randomMovement)));
      return newPosition;
    }

    // No special action on this tile
    return tileId;
  }

  /**
   * Checks if a player has won the game
   * 
   * @param player The player to check
   * @return True if the player has won
   */
  @Override
  public boolean checkVictory(Player player) {
    LadderBoard board = getLadderBoard();
    int winningPosition = board.getRows() * board.getColumns();
    return player.getTileId() >= winningPosition;
  }

  /**
   * Checks if a given tile has a special action
   * 
   * @param tileId The tile ID to check
   * @return True if the tile has a special action
   */
  public boolean hasTileAction(int tileId) {
    LadderGameTile tile = getLadderBoard().getTile(tileId);
    return tile != null && tile.hasAction();
  }

  @Override
  public LadderBoard getGameBoard() {
    return (LadderBoard) super.getGameBoard();
  }

  /**
   * Gets the destination tile ID for a special tile
   * 
   * @param tileId The source tile ID
   * @return The destination tile ID, or the source if no action exists
   */
  public int getActionDestination(int tileId) {
    LadderGameTile tile = getLadderBoard().getTile(tileId);
    if (tile == null)
      return tileId;

    if (tile.hasLadder()) {
      return tile.getLadder().getNumber();
    } else if (tile.hasSnake()) {
      return tile.getSnake().getNumber();
    } else if (tile.hasWormhole()) {
      return tile.getWormhole().getNumber();
    }

    return tileId;
  }

  // Getters and setters for event listeners

  public void setOnGameWon(Runnable onGameWon) {
    this.onGameWon = onGameWon;
  }

  public void setOnDiceRolled(RollResultCallback onDiceRolled) {
    this.onDiceRolled = onDiceRolled;
  }

  public void setOnPlayerMovement(PlayerMoveCallback onPlayerMovement) {
    this.onPlayerMovement = onPlayerMovement;
  }
}