package edu.ntnu.idi.idatt.model;

public class LadderGameTile extends Tile {
  private LadderGameTile snake; // The tile at the tail end of the snake
  private LadderGameTile ladder; // The tile at the top end of the ladder
  private LadderGameTile wormhole; // The tile at the destination of the wormhole

  /**
   * Constructor with tile number
   * 
   * @param number The tile number
   */
  public LadderGameTile(int number) {
    super(number);
  }

  /**
   * Sets the tile that this snake leads to (tail of the snake)
   * 
   * @param snake The tile at the tail end of the snake
   */
  public void setSnake(LadderGameTile snake) {
    this.snake = snake;
  }

  /**
   * Sets the tile that this ladder leads to (top of the ladder)
   * 
   * @param ladder The tile at the top end of the ladder
   */
  public void setLadder(LadderGameTile ladder) {
    this.ladder = ladder;
  }

  /**
   * Sets the tile that this wormhole leads to (destination of the wormhole)
   * 
   * @param wormhole The tile at the destination of the wormhole
   */
  public void setWormhole(LadderGameTile wormhole) {
    this.wormhole = wormhole;
  }

  /**
   * Gets the tile that the snake on this tile leads to
   * 
   * @return The tile at the end of the snake, or null if no snake
   */
  public LadderGameTile getSnake() {
    return snake;
  }

  /**
   * Gets the tile that the ladder on this tile leads to
   * 
   * @return The tile at the top of the ladder, or null if no ladder
   */
  public LadderGameTile getLadder() {
    return ladder;
  }

  /**
   * Gets the tile that the wormhole on this tile leads to
   * 
   * @return The tile at the destination of the wormhole, or null if no wormhole
   */
  public LadderGameTile getWormhole() {
    return wormhole;
  }

  /**
   * Checks if this tile has a snake
   * 
   * @return true if this tile has a snake, false otherwise
   */
  public boolean hasSnake() {
    return snake != null;
  }

  /**
   * Checks if this tile has a ladder
   * 
   * @return true if this tile has a ladder, false otherwise
   */
  public boolean hasLadder() {
    return ladder != null;
  }

  /**
   * Checks if this tile has a wormhole
   * 
   * @return true if this tile has a wormhole, false otherwise
   */
  public boolean hasWormhole() {
    return wormhole != null;
  }

  /**
   * Checks if this tile has an action
   * 
   * @return true if this tile has an action, false otherwise
   */
  public boolean hasAction() {
    return hasSnake() || hasLadder() || hasWormhole();
  }
}