package edu.ntnu.idi.idatt.model;

/**
 * The Tile class represents a tile in a game or application.
 * Each tile has a unique identifier and can have an associated action.
 */
public class Tile {

  /**
   * The unique identifier for this tile.
   */
  private final int tileId;

  /**
   * The action to be performed when landing on this tile.
   */
  private TileAction landAction;

  /**
   * Indicates whether this tile has an associated action.
   */
  private boolean hasAction;

  /**
   * Constructs a Tile with the specified identifier.
   *
   * @param tileId the unique identifier for this tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Constructs a Tile with the specified identifier and action.
   *
   * @param tileId the unique identifier for this tile
   * @param action the action to be performed when landing on this tile
   */
  public Tile(int tileId, TileAction action) {
    this.tileId = tileId;
    this.landAction = action;
    this.hasAction = false;
  }

  /**
   * Returns the unique identifier for this tile.
   *
   * @return the unique identifier for this tile
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Sets the action to be performed when landing on this tile.
   *
   * @param action the action to be performed when landing on this tile
   */
  public void setAction(TileAction action) {
    this.landAction = action;
    this.hasAction = true;
  }

  /**
   * Checks if this tile has an associated action.
   *
   * @return true if this tile has an associated action, false otherwise
   */
  public boolean hasAction() {
    return this.hasAction;
  }

  /**
   * Returns the action to be performed when landing on this tile.
   *
   * @return the action to be performed when landing on this tile
   */
  public TileAction getLandAction() {
    return landAction;
  }
}
