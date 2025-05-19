package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;

/**
 * AbstractBoard provides a base implementation for board games.
 * Contains common functionality that any board game would need.
 *
 * @param <T> The type of tile used on this board
 */
public abstract class AbstractBoard<T extends Tile> {
  private String name;
  private String description;
  protected List<T> tiles;
  private List<GameObserver> observers;

  /**
   * Creates a new board with the specified dimensions.
   * 
   * @param rows    Number of rows in the board
   * @param columns Number of columns in the board
   */
  public AbstractBoard() {
    this.tiles = new ArrayList<>();
    this.observers = new ArrayList<>();
    initializeTiles();
  }

  /**
   * Initialize the tiles for this board.
   * Each board game implementation will define its own initialization logic.
   */
  protected abstract void initializeTiles();

  /**
   * Gets a tile by its number.
   *
   * @param number The tile number
   * @return The tile with the specified number
   * @throws IllegalArgumentException if the tile number is invalid
   */
  public T getTile(int number) {
    if (number < 1 || number > getNumberOfTiles()) {
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
   * Validates a tile number.
   *
   * @param tileNumber The tile number to validate
   * @throws IllegalArgumentException if the tile number is invalid
   */
  protected void validateTileNumber(int tileNumber) {
    if (tileNumber < 1 || tileNumber > getNumberOfTiles()) {
      throw new IllegalArgumentException("Invalid tile number: " + tileNumber);
    }
  }

  /**
   * Adds an observer to the board.
   * 
   * @param observer The observer to add
   */
  public void addObserver(GameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
    }
  }

  /**
   * Removes an observer from the board.
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
  protected void notifyObservers(GameEvent event) {
    for (GameObserver observer : observers) {
      observer.update(event);
    }
  }

  // Getters and setters

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

  public List<T> getTiles() {
    return new ArrayList<>(tiles); // Return a defensive copy
  }

  public void setTiles(List<T> tiles) {
    this.tiles = new ArrayList<>(tiles); // Create a defensive copy
    notifyObservers(new GameEvent("TILES_CHANGED", this.tiles));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    AbstractBoard<?> that = (AbstractBoard<?>) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(description, that.description) &&
        Objects.equals(tiles, that.tiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, tiles);
  }

}