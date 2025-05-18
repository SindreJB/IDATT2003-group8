package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;

/**
 * Represents a game board with a collection of tiles arranged in a grid.
 * Designed to be easily serializable to/from JSON.
 */
public class Board {
  private String name;
  private String description;
  private int rows;
  private int columns;
  private List<LadderGameTile> tiles;
  private List<GameObserver> observers;

  /**
   * Creates a new board with the specified dimensions.
   * 
   * @param rows    Number of rows in the board
   * @param columns Number of columns in the board
   */
  public Board(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    this.tiles = new ArrayList<>();
    this.observers = new ArrayList<>();
    initializeTiles();
  }

  /**
   * Initialize tiles in a snake pattern (like traditional Snakes and Ladders).
   * For example, in a 3x3 board:
   * 7 8 9
   * 6 5 4
   * 1 2 3
   */
  private void initializeTiles() {
    tiles.clear();
    int totalTiles = rows * columns;

    for (int i = 0; i < totalTiles; i++) {
      int row = i / columns;
      int col = i % columns;

      // For even rows, numbers go left to right
      // For odd rows, numbers go right to left
      int tileNumber = row % 2 == 0 ? row * columns + col + 1 : (row + 1) * columns - col;

      LadderGameTile tile = new LadderGameTile(tileNumber);
      // Calculate and set the x,y coordinates for UI positioning
      tiles.add(tile);
    }
  }

  /**
   * Gets a tile by its number (1-based indexing).
   *
   * @param number The tile number to retrieve
   * @return The tile with the specified number
   * @throws IllegalArgumentException if the tile number is invalid
   */
  public LadderGameTile getTile(int number) {
    if (number < 1 || number > tiles.size()) {
      throw new IllegalArgumentException("Invalid tile number: " + number);
    }
    return tiles.stream()
        .filter(t -> t.getNumber() == number)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Tile not found: " + number));
  }

  /**
   * Gets the total number of tiles on the board.
   *
   * @return The total number of tiles
   */
  public int getNumberOfTiles() {
    return tiles.size();
  }

  /**
   * Adds an observer to the board's observer list.
   * 
   * @param observer The observer to add
   */
  public void addObserver(GameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
    }
  }

  /**
   * Removes an observer from the board's observer list.
   * 
   * @param observer The observer to remove
   */
  public void removeObserver(GameObserver observer) {
    observers.remove(observer);
  }

  /**
   * Notifies all observers of a game event.
   * 
   * @param event The game event to notify observers about
   */
  public void notifyObservers(GameEvent event) {
    for (GameObserver observer : observers) {
      observer.update(event);
    }
  }

  // Getters and setters for JSON serialization

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    notifyObservers(new GameEvent("BOARD_NAME_CHANGED", name));
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
    notifyObservers(new GameEvent("BOARD_DESCRIPTION_CHANGED", description));
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public List<LadderGameTile> getTiles() {
    return tiles;
  }

  public void setTiles(List<LadderGameTile> tiles) {
    this.tiles = tiles;
    notifyObservers(new GameEvent("TILES_CHANGED", tiles));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Board board = (Board) o;
    return rows == board.rows &&
        columns == board.columns &&
        Objects.equals(name, board.name) &&
        Objects.equals(description, board.description) &&
        Objects.equals(tiles, board.tiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, rows, columns, tiles);
  }
}