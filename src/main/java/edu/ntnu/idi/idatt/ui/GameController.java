package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.factory.LadderGameFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;
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
  private Map<GameObserver, List<String>> observerEventTypes = new HashMap<>();

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
      // Notify observers about the new board
      gameBoard.notifyObservers(new GameEvent("BOARD_LOADED", gameBoard));
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
      
      // Notify observers about game setup
      if (gameBoard != null) {
        gameBoard.notifyObservers(new GameEvent("GAME_SETUP", this.players));
      }
    }
  }

  /**
   * Registers an observer for specific event types
   * 
   * @param observer The observer to register
   * @param eventTypes The event types the observer is interested in, or empty for all events
   */
  public void registerObserver(GameObserver observer, String... eventTypes) {
    if (observer == null) return;
    
    // Add observer to the board
    if (gameBoard != null) {
      gameBoard.addObserver(observer);
    }
    
    // Track event types this observer is interested in
    if (eventTypes != null && eventTypes.length > 0) {
      List<String> types = observerEventTypes.getOrDefault(observer, new ArrayList<>());
      for (String type : eventTypes) {
        types.add(type);
      }
      observerEventTypes.put(observer, types);
    }
  }
  
  /**
   * Unregisters an observer
   * 
   * @param observer The observer to unregister
   */
  public void unregisterObserver(GameObserver observer) {
    if (observer == null) return;
    
    // Remove from board
    if (gameBoard != null) {
      gameBoard.removeObserver(observer);
    }
    
    // Remove from tracking map
    observerEventTypes.remove(observer);
  }
  
  /**
   * Notifies observers about a game event
   * 
   * @param event The event to notify about
   */
  private void notifyObservers(GameEvent event) {
    if (gameBoard != null) {
      gameBoard.notifyObservers(event);
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
    
    // Notify observers about dice roll
    notifyObservers(new GameEvent("DICE_ROLLED", diceValue));

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
      
      // Notify about ladder event
      notifyObservers(new GameEvent("LADDER_CLIMBED", 
          Map.of("player", currentPlayer, "from", landedPosition, "to", newPosition)));
      
    } else if (gameBoard.getTile(newPosition).hasSnake()) {
      newPosition = gameBoard.getTile(newPosition).getSnake().getNumber();
      message = currentPlayer.getName() + " rolled " + diceValue +
          " and slid down a snake from " + landedPosition + " to " + newPosition;
      
      // Notify about snake event
      notifyObservers(new GameEvent("SNAKE_SLIDE", 
          Map.of("player", currentPlayer, "from", landedPosition, "to", newPosition)));
      
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
      
      // Notify about wormhole event
      notifyObservers(new GameEvent("WORMHOLE_TELEPORT", 
          Map.of("player", currentPlayer, "from", landedPosition, "to", newPosition, "movement", randomMovement)));
    } else {
      // No special tile
      message = currentPlayer.getName() + " rolled " + diceValue +
          " and moved from " + oldPosition + " to " + newPosition;
    }

    // Update player position
    currentPlayer.setTileId(newPosition);
    
    // Notify about player movement
    notifyObservers(new GameEvent("PLAYER_MOVED", 
        Map.of("player", currentPlayer, "from", oldPosition, "to", newPosition, "diceValue", diceValue)));

    // Notify listeners via callbacks (legacy)
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
      // Notify observers about game won event
      notifyObservers(new GameEvent("GAME_WON", currentPlayer));
      
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
    
    // Notify observers about turn change
    notifyObservers(new GameEvent("TURN_CHANGED", currentPlayer));

    // Legacy notification
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
    
    // Notify observers about game reset
    notifyObservers(new GameEvent("GAME_RESET", players));

    // Legacy notification
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
      // Notify observers about saving players
      notifyObservers(new GameEvent("PLAYERS_SAVED", filePath));
      return true;
    } catch (IOException e) {
      System.err.println("Error saving players: " + e.getMessage());
      notifyObservers(new GameEvent("ERROR", "Error saving players: " + e.getMessage()));
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
        notifyObservers(new GameEvent("ERROR", "No players found in file"));
        return false;
      }

      this.players = loadedPlayers;
      currentPlayer = players.get(0);
      currentPlayerIndex = 0;
      
      // Notify observers about loaded players
      notifyObservers(new GameEvent("PLAYERS_LOADED", loadedPlayers));

      return true;
    } catch (IOException e) {
      System.err.println("Error loading players: " + e.getMessage());
      notifyObservers(new GameEvent("ERROR", "Error loading players: " + e.getMessage()));
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