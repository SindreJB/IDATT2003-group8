package edu.ntnu.idi.idatt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureBoardConfig;
import edu.ntnu.idi.idatt.model.TreasureGameTile;
import edu.ntnu.idi.idatt.observer.GameEvent;

public class TreasureGameController extends GameController {
  private boolean treasureFound = false;
  private int moveCounter = 0;
  private boolean isMoving = false;
  private Runnable onStepComplete;
  private int currentPlayerIndex = 0;
  private Player currentPlayer;
  private boolean manualMovementMode = true;
  private final TreasureBoardConfig config;

  public TreasureGameController() {
    super(1);
    this.config = new TreasureBoardConfig();
    loadBoard(new TreasureBoard(config.ROWS, config.COLUMNS));
  }

  public void loadBoard(TreasureBoard board) {
    this.gameBoard = board;
    board.initializeBoardWithTreasure();
  }

  @Override
  public TreasureBoard getGameBoard() {
    return (TreasureBoard) super.getGameBoard();
  }

  /**
   * Sets the callback to be called when a step movement is complete
   * 
   * @param onStepComplete The callback to call
   */
  public void setOnStepComplete(Runnable onStepComplete) {
    this.onStepComplete = onStepComplete;
  }

  /**
   * Gets the current move counter value
   * 
   * @return The number of moves remaining
   */
  public int getMoveCounter() {
    return moveCounter;
  }

  /**
   * Checks if player is currently in the middle of a move sequence
   * 
   * @return True if player is moving, false otherwise
   */
  public boolean isMoving() {
    return isMoving;
  }

  /**
   * Roll dice and start step-by-step movement
   * 
   * @return The dice roll value
   */
  public int rollDiceAndMove() {
    // Prevent multiple rolls while moving
    if (isMoving) {
      return 0;
    }

    // Roll the dice
    int diceValue = rollDice();
    moveCounter = diceValue;
    isMoving = true;

    // Notify observers about move counter
    notifyObservers(new GameEvent("MOVE_COUNTER_UPDATED", moveCounter));

    // If in manual mode, don't start automatic movement
    if (!manualMovementMode) {
      // Start step-by-step movement
      executeNextStep();
    }

    return diceValue;
  }

  /**
   * Execute the next step in the movement sequence
   */
  public void executeNextStep() {
    if (moveCounter <= 0) {
      finishMovement();
      return;
    }
    Player player = getCurrentPlayer();
    if (player == null) {
      if (!players.isEmpty()) {
        player = players.get(0);
        this.currentPlayer = player;
        this.currentPlayerIndex = 0;
      } else {
        // No players available
        finishMovement();
        return;
      }
    }
    int oldPosition = player.getTileId();

    int newPosition = findNextPosition(oldPosition);

    if (newPosition == oldPosition) {
      finishMovement();
      return;
    }

    movePlayer(player, oldPosition, newPosition);

    processTileActions(player, newPosition);

    moveCounter--;
    notifyObservers(new GameEvent("MOVE_COUNTER_UPDATED", moveCounter));

    boolean hasWon = checkVictory(player);
    if (hasWon) {
      notifyObservers(new GameEvent("GAME_WON", player));
      isMoving = false;
      return;
    }

    if (onStepComplete != null) {
      onStepComplete.run();
    }
  }

  /**
   * Finish the movement sequence and pass to next player
   */
  private void finishMovement() {
    isMoving = false;
    moveCounter = 0;
    notifyObservers(new GameEvent("MOVE_COUNTER_UPDATED", moveCounter));

    // If player hasn't won, switch to next player
    if (!treasureFound) {
      Player current = getCurrentPlayer();
      // Check if player found a treasure on their last move
      if (current != null) {
        int tileId = current.getTileId();
        if (config.getTileType(tileId) == 2) {
          // Check if this is the tile with the real treasure
          TreasureBoard board = getGameBoard();
          TreasureGameTile tile = board.getTile(tileId);

          if (tile.hasTreasure()) {
            // Player found the real treasure (Star of Africa)!
            treasureFound = true;
            notifyObservers(new GameEvent("TREASURE_FOUND",
                Map.of("player", current, "position", tileId, "realTreasure", true)));
          } else {
            // Player dug up nothing but dirt
            notifyObservers(new GameEvent("TREASURE_FOUND",
                Map.of("player", current, "position", tileId, "realTreasure", false)));
          }
          return;
        }
      }
      switchToNextPlayer();
    }
  }

  /**
   * Find the next valid position based on the board layout
   * For step-by-step movement, only adjacent tiles are considered
   * 
   * @param currentPosition The current position
   * @return The next valid position
   */
  private int findNextPosition(int currentPosition) {
    // Get coordinates using the config
    int[] coords = config.getCoordinates(currentPosition);
    if (coords == null) {
      return currentPosition;
    }

    int row = coords[0];
    int col = coords[1];

    // For step movement, we only want to move to adjacent tiles
    // Define possible directions (right, down, left, up)
    int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    // Try each direction
    for (int[] dir : directions) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];

      // Get new tile ID using the config
      int newTileId = config.getTileId(newRow, newCol);

      // Check if walkable using the config
      if (newTileId != -1 && config.isWalkable(newTileId)) {
        return newTileId;
      }
    }

    // If no valid move is found, stay in place
    return currentPosition;
  }

  @Override
  public int processTileActions(Player player, int tileId) {
    // Check if this is a treasure location (type 2) AND we have 0 moves left
    if (config.getTileType(tileId) == 2 && moveCounter == 0) {
      TreasureBoard board = getGameBoard();
      TreasureGameTile tile = board.getTile(tileId);

      if (tile.hasTreasure()) {
        // Player found the real treasure (Star of Africa)!
        notifyObservers(new GameEvent("TREASURE_FOUND",
            Map.of("player", player, "position", tileId, "realTreasure", true)));
        treasureFound = true;
      } else {
        // Player dug up nothing but dirt
        notifyObservers(new GameEvent("TREASURE_FOUND",
            Map.of("player", player, "position", tileId, "realTreasure", false)));
      }
    } else if (config.getTileType(tileId) == 2) {
      // Player is on a treasure tile but has moves left
      notifyObservers(new GameEvent("TREASURE_TILE",
          Map.of("player", player, "position", tileId, "movesLeft", moveCounter)));
    }

    return tileId;
  }

  @Override
  public boolean checkVictory(Player player) {
    return treasureFound;
  }

  public boolean isTileWalkable(int tileId) {
    // Use the config class for walkability check to ensure consistency
    return config.isWalkable(tileId);
  }

  /**
   * Checks if a move to the specified position is valid for the current player
   * 
   * @param direction Direction string: "UP", "DOWN", "LEFT", "RIGHT"
   * @return The new position if valid, -1 if invalid
   */
  public int getValidPositionInDirection(String direction) {
    if (moveCounter <= 0) {
      return -1;
    }

    Player player = getCurrentPlayer();
    int currentPosition = player.getTileId();

    int[] coords = config.getCoordinates(currentPosition);
    if (coords == null) {
      return -1;
    }

    int row = coords[0];
    int col = coords[1];

    int newRow = row;
    int newCol = col;

    switch (direction) {
      case "UP" -> newRow = row - 1;
      case "DOWN" -> newRow = row + 1;
      case "LEFT" -> newCol = col - 1;
      case "RIGHT" -> newCol = col + 1;
    }

    int newTileId = config.getTileId(newRow, newCol);
    if (newTileId == -1) {
      return -1;
    }

    if (config.isWalkable(newTileId)) {
      return newTileId;
    }

    return -1;
  }

  /**
   * Manually move the current player in the specified direction
   * 
   * @param direction Direction string: "UP", "DOWN", "LEFT", "RIGHT"
   * @return true if the move was successful, false otherwise
   */
  public boolean movePlayerInDirection(String direction) {
    if (!isMoving || moveCounter <= 0) {
      return false;
    }

    int newPosition = getValidPositionInDirection(direction);
    if (newPosition == -1) {
      return false; // Invalid move
    }

    Player playerToMove = getCurrentPlayer();
    int oldPosition = playerToMove.getTileId();

    // Move player to new position
    movePlayer(playerToMove, oldPosition, newPosition);

    // Process any special tile actions
    processTileActions(playerToMove, newPosition);

    // Decrement move counter
    moveCounter--;
    notifyObservers(new GameEvent("MOVE_COUNTER_UPDATED", moveCounter));

    // Check for victory
    boolean hasWon = checkVictory(playerToMove);
    if (hasWon) {
      notifyObservers(new GameEvent("GAME_WON", playerToMove));
      isMoving = false;
      return true;
    }

    // If no more moves left, finish movement
    if (moveCounter <= 0) {
      finishMovement();
    }

    return true;
  }

  /**
   * Sets whether the game is in manual movement mode
   * 
   * @param manualMode true for manual movement, false for automatic
   */
  public void setManualMovementMode(boolean manualMode) {
    this.manualMovementMode = manualMode;
  }

  /**
   * Checks if the game is in manual movement mode
   * 
   * @return true if in manual mode, false if in automatic mode
   */
  public boolean isManualMovementMode() {
    return manualMovementMode;
  }

  @Override
  public Player getCurrentPlayer() {
    if (this.currentPlayer == null && !players.isEmpty()) {
      this.currentPlayer = players.get(0);
      this.currentPlayerIndex = 0;
    }
    return this.currentPlayer;
  }

  @Override
  public void switchToNextPlayer() {
    if (players.isEmpty())
      return;

    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentPlayer = players.get(currentPlayerIndex);

    notifyObservers(new GameEvent("TURN_CHANGED", currentPlayer));
  }

  @Override
  public void setupGame(List<Player> players) {
    if (players != null && !players.isEmpty()) {
      this.players = new ArrayList<>(players);

      // Find the actual start position (tile with type 3)
      int startPosition = config.findStartPosition();

      // Ensure all players start at the correct start position
      for (Player player : this.players) {
        player.setTileId(startPosition);
      }

      // Set first player as current
      this.currentPlayerIndex = 0;
      this.currentPlayer = this.players.get(0);

      // Notify observers about game setup
      notifyObservers(new GameEvent("GAME_SETUP", this.players));
    }
  }

  @Override
  public void resetGame() {
    // Find the correct start position
    int startPosition = config.findStartPosition();

    // Reset player positions to start position
    for (Player player : players) {
      player.setTileId(startPosition);
    }

    // Reset game state
    treasureFound = false;
    moveCounter = 0;
    isMoving = false;

    currentPlayerIndex = 0;
    if (!players.isEmpty()) {
      currentPlayer = players.get(0);
    }

    // Reset the treasure position by reassigning it randomly
    TreasureBoard board = getGameBoard();

    // Reset all tiles to not have treasure
    for (int i = 1; i <= 100; i++) {
      TreasureGameTile tile = board.getTile(i);
      if (tile != null) {
        tile.setHasTreasure(false);
      }
    }

    // Assign a new random treasure
    board.assignRandomTreasure();

    notifyObservers(new GameEvent("GAME_RESET", players));
  }
}