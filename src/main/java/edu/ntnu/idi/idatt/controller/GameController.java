package edu.ntnu.idi.idatt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.model.AbstractBoard;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;
import edu.ntnu.idi.idatt.persistence.CsvHandler;

/**
 * Abstract controller class for board games.
 * Handles common game functionality like player management, turn handling,
 * dice rolling, and observer pattern implementation.
 */
public abstract class GameController {
  public AbstractBoard<? extends Tile> gameBoard;
  public List<Player> players;  private Player currentPlayer;
  private int currentPlayerIndex;
  private final Dice dice;
  private final Map<GameObserver, List<String>> observerEventTypes;

  /**
   * Creates a new game controller with default configuration
   */
  public GameController() {
    this(2); // Default to 2 dice for backward compatibility
  }
  
  /**
   * Creates a new game controller with specified number of dice
   * 
   * @param diceCount The number of dice to use in the game
   */
  public GameController(int diceCount) {
    this.players = new ArrayList<>();
    this.dice = new Dice(diceCount);
    this.currentPlayerIndex = 0;
    this.observerEventTypes = new HashMap<>();
  }

  /**
   * Gets the number of dice used in this game
   * 
   * @return The number of dice
   */
  public int getDiceCount() {
    return dice.getDiceCount();
  }

  /**
   * Loads a game board
   * 
   * @param board The board to load
   */
  public void loadBoard(AbstractBoard<? extends Tile> board) {
    this.gameBoard = board;
    notifyObservers(new GameEvent("BOARD_LOADED", board));
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
      this.currentPlayerIndex = 0;
      this.currentPlayer = this.players.get(0);

      // Notify observers about game setup
      notifyObservers(new GameEvent("GAME_SETUP", this.players));
    }
  }

  /**
   * Adds a player to the game
   * 
   * @param player The player to add
   */
  public void addPlayer(Player player) {
    if (player != null && !players.contains(player)) {
      players.add(player);
      notifyObservers(new GameEvent("PLAYER_ADDED", player));
    }
  }

  /**
   * Removes a player from the game
   * 
   * @param player The player to remove
   */
  public void removePlayer(Player player) {
    if (players.remove(player)) {
      notifyObservers(new GameEvent("PLAYER_REMOVED", player));
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
  }

  /**
   * Rolls the dice
   * 
   * @return The dice value
   */
  public int rollDice() {
    int value = dice.rollDice();
    notifyObservers(new GameEvent("DICE_ROLLED", value));
    return value;
  }

  /**
   * Moves a player to a new position
   * 
   * @param player      The player to move
   * @param newPosition The new position
   */
  public void movePlayer(Player player, int oldPosition, int newPosition) {
    player.setTileId(newPosition);

    Map<String, Object> moveData = new HashMap<>();
    moveData.put("player", player);
    moveData.put("from", oldPosition);
    moveData.put("to", newPosition);

    notifyObservers(new GameEvent("PLAYER_MOVED", moveData));
  }

  /**
   * Process any actions associated with a tile
   * 
   * @param player The player who landed on the tile
   * @param tileId The tile ID to process
   * @return The new position after processing tile actions
   */
  public abstract int processTileActions(Player player, int tileId);

  /**
   * Checks if a player has won the game
   * 
   * @param player The player to check
   * @return true if the player has won, false otherwise
   */
  public abstract boolean checkVictory(Player player);

  /**
   * Switches to the next player in turn
   */
  public void switchToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentPlayer = players.get(currentPlayerIndex);

    notifyObservers(new GameEvent("TURN_CHANGED", currentPlayer));
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
      notifyObservers(new GameEvent("PLAYERS_SAVED", filePath));
      return true;
    } catch (IOException e) {
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

      notifyObservers(new GameEvent("PLAYERS_LOADED", loadedPlayers));
      return true;
    } catch (IOException e) {
      notifyObservers(new GameEvent("ERROR", "Error loading players: " + e.getMessage()));
      return false;
    }
  }

  /**
   * Registers an observer for specific event types
   * 
   * @param observer   The observer to register
   * @param eventTypes The event types the observer is interested in, or empty for
   *                   all events
   */
  public void registerObserver(GameObserver observer, String... eventTypes) {
    if (observer == null)
      return;

    // Track event types this observer is interested in
    if (eventTypes != null && eventTypes.length > 0) {
      List<String> types = observerEventTypes.getOrDefault(observer, new ArrayList<>());
      for (String type : eventTypes) {
        types.add(type);
      }
      observerEventTypes.put(observer, types);
    } else {
      // No specific event types means observer wants all events
      observerEventTypes.put(observer, null);
    }
  }

  /**
   * Unregisters an observer
   * 
   * @param observer The observer to unregister
   */
  public void unregisterObserver(GameObserver observer) {
    if (observer != null) {
      observerEventTypes.remove(observer);
    }
  }

  /**
   * Notifies observers about a game event
   * 
   * @param event The event to notify about
   */
  public void notifyObservers(GameEvent event) {
    for (Map.Entry<GameObserver, List<String>> entry : observerEventTypes.entrySet()) {
      GameObserver observer = entry.getKey();
      List<String> eventTypes = entry.getValue();

      if (eventTypes == null || eventTypes.contains(event.getType())) {
        observer.update(event);
      }
    }
  }

  // Getters for game state

  public AbstractBoard<? extends Tile> getGameBoard() {
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