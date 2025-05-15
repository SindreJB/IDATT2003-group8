package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.factory.LadderGameFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.persistence.CsvHandler;

/**
 * The GameController class manages the game logic and flow for the Snakes and
 * Ladders game.
 * It follows the MVC pattern by separating game logic from UI representation.
 */
public class GameController {

  private Board gameBoard;
  private List<Player> players = new ArrayList<>();
  private Player currentPlayer;
  private int currentPlayerIndex = 0;
  private Dice dice = new Dice();

  // Event listeners/callbacks for UI updates
  private Runnable onPlayerMoved;
  private Runnable onTurnChanged;
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
   * Creates a new GameController with a default board
   */
  public GameController() {
    this.gameBoard = new Board(10, 9);
  }

  /**
   * Loads a board based on its type
   * 
   * @param boardType The type of board to load
   * @return True if loading was successful
   */
  public boolean loadBoard(String boardType) {
    Optional<Board> loadedBoard = LadderGameFactory.tryCreateBoard(boardType);
    if (loadedBoard.isPresent()) {
      gameBoard = loadedBoard.get();
      return true;
    }
    return false;
  }

  /**
   * Sets up the game with the provided list of players
   * 
   * @param players The list of players for the game
   */
  public void setupGame(List<Player> players) {
    if (players != null && !players.isEmpty()) {
      this.players = new ArrayList<>(players);

      // Ensure all players start at position 1
      for (Player player : this.players) {
        player.setTileId(1);
      }

      // Set first player as current
      currentPlayerIndex = 0;
      currentPlayer = this.players.get(0);
    }
  }

  /**
   * Performs dice rolling and player movement logic
   * 
   * @return The dice roll value
   */
  public int rollDiceAndMove() {
    // Roll the dice
    int diceValue = dice.rollDice();

    // Get player's current position
    int oldPosition = currentPlayer.getTileId();

    // Calculate new position within board limits
    int newPosition = Math.min(oldPosition + diceValue,
        gameBoard.getRows() * gameBoard.getColumns());

    // Store landed position before checking for special tiles
    int landedPosition = newPosition;
    String message = "";

    // Check for special tiles and handle their effects
    if (gameBoard.getTile(newPosition).hasLadder()) {
      newPosition = gameBoard.getTile(newPosition).getLadder().getNumber();
      message = currentPlayer.getName() + " rolled " + diceValue +
          " and climbed a ladder from " + landedPosition + " to " + newPosition;
    } else if (gameBoard.getTile(newPosition).hasSnake()) {
      newPosition = gameBoard.getTile(newPosition).getSnake().getNumber();
      message = currentPlayer.getName() + " rolled " + diceValue +
          " and slid down a snake from " + landedPosition + " to " + newPosition;
    } else if (gameBoard.getTile(newPosition).hasWormhole()) {
      // For wormholes, generate a random movement between -15 and +20 tiles
      int randomMovement = new Random().nextInt(36) - 15;

      int wormholeResult = Math.max(1, newPosition + randomMovement);
      newPosition = wormholeResult;

      // Create descriptive message based on the random movement
      if (randomMovement > 0) {
        message = currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you forward " + randomMovement +
            " spaces to " + newPosition + "!";
      } else if (randomMovement < 0) {
        message = currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you backward " + Math.abs(randomMovement) +
            " spaces to " + newPosition + "!";
      } else {
        message = currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole spun you around but left you in the same place!";
      }
    } else {
      // No special tile
      message = currentPlayer.getName() + " rolled " + diceValue +
          " and moved from " + oldPosition + " to " + newPosition;
    }

    // Update player position
    currentPlayer.setTileId(newPosition);

    // Notify listeners
    if (onDiceRolled != null) {
      onDiceRolled.onDiceRolled(diceValue, message, oldPosition, newPosition);
    }

    // Notify about player movement (for UI animation)
    boolean hasWon = checkVictory(currentPlayer);
    if (onPlayerMovement != null) {
      onPlayerMovement.onPlayerMovement(currentPlayer, oldPosition, newPosition, hasWon);
    }

    // Check for victory
    if (hasWon) {
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
   * Checks if a player has won the game
   * 
   * @param player The player to check
   * @return True if the player has won
   */
  public boolean checkVictory(Player player) {
    // The last tile on the board is the winning position
    int winningPosition = gameBoard.getRows() * gameBoard.getColumns();
    return player.getTileId() >= winningPosition;
  }

  /**
   * Switches to the next player in turn
   */
  public void switchToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentPlayer = players.get(currentPlayerIndex);

    if (onTurnChanged != null) {
      onTurnChanged.run();
    }
  }

  /**
   * Resets the game with the same players
   */
  public void resetGame() {
    // Reset player positions to start position
    for (Player player : players) {
      player.setTileId(1);
    }

    // Reset turn to first player
    currentPlayerIndex = 0;
    currentPlayer = players.get(0);

    if (onTurnChanged != null) {
      onTurnChanged.run();
    }
  }

  /**
   * Checks if a given tile has a special action (snake, ladder, or wormhole)
   * 
   * @param tileId The tile ID to check
   * @return True if the tile has a special action
   */
  public boolean hasTileAction(int tileId) {
    Tile tile = gameBoard.getTile(tileId);
    return tile != null && tile.hasAction();
  }

  /**
   * Gets the destination tile ID for a snake/ladder/wormhole
   * 
   * @param tileId The source tile ID
   * @return The destination tile ID, or the source if no action exists
   */
  public int getActionDestination(int tileId) {
    Tile tile = gameBoard.getTile(tileId);
    if (tile == null)
      return tileId;

    if (tile.hasLadder()) {
      return tile.getLadder().getNumber();
    } else if (tile.hasSnake()) {
      return tile.getSnake().getNumber();
    } else if (tile.hasWormhole()) {
      // For wormholes, return the destination ID
      return tile.getWormhole().getNumber();
    }

    return tileId;
  }

  /**
   * Saves the current players to a CSV file
   * 
   * @param filePath The path to save the CSV file
   * @return True if successful
   */
  public boolean savePlayersToFile(String filePath) {
    try {
      CsvHandler.savePlayersToCsv(players, filePath);
      return true;
    } catch (IOException e) {
      System.err.println("Error saving players: " + e.getMessage());
      return false;
    }
  }

  /**
   * Loads players from a CSV file
   * 
   * @param filePath The path to the CSV file
   * @return True if successful
   */
  public boolean loadPlayersFromFile(String filePath) {
    try {
      List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv(filePath);

      if (loadedPlayers.isEmpty()) {
        return false;
      }

      this.players = loadedPlayers;
      currentPlayer = players.get(0);
      currentPlayerIndex = 0;

      return true;
    } catch (IOException e) {
      System.err.println("Error loading players: " + e.getMessage());
      return false;
    }
  }

  // Getters and setters for event listeners

  public void setOnPlayerMoved(Runnable onPlayerMoved) {
    this.onPlayerMoved = onPlayerMoved;
  }

  public void setOnTurnChanged(Runnable onTurnChanged) {
    this.onTurnChanged = onTurnChanged;
  }

  public void setOnGameWon(Runnable onGameWon) {
    this.onGameWon = onGameWon;
  }

  public void setOnDiceRolled(RollResultCallback onDiceRolled) {
    this.onDiceRolled = onDiceRolled;
  }

  public void setOnPlayerMovement(PlayerMoveCallback onPlayerMovement) {
    this.onPlayerMovement = onPlayerMovement;
  }

  // Getters for game state

  public Board getGameBoard() {
    return gameBoard;
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}