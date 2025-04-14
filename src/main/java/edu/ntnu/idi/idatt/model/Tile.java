package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.model.TileAction;

import java.util.Objects;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TileAction;

/**
 * Represents a single tile on the game board.
 * Each tile has a number, coordinates, and potentially an action.
 */
public class Tile {
  private int number; // The tile number (1-based)
  private int x; // X coordinate on the board
  private int y; // Y coordinate on the board
  private TileAction action; // The action associated with this tile (can be null)

  /**
   * Creates a new tile with the specified number.
   *
   * @param number The tile number
   */
  public Tile(int number) {
    this.number = number;
  }

  /**
   * Executes this tile's action on a player, if an action exists.
   *
   * @param player The player to perform the action on
   * @return true if an action was performed, false otherwise
   */
  public boolean performAction(Player player) {
    if (action != null) {
      action.perform(player);
      return true;
    }
    return false;
  }

  // Getters and setters for JSON serialization

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public TileAction getAction() {
    return action;
  }

  public void setAction(TileAction action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Tile tile = (Tile) o;
    return number == tile.number &&
        x == tile.x &&
        y == tile.y &&
        Objects.equals(action, tile.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, x, y, action);
  }
}