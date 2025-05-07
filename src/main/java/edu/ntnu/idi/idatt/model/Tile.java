package edu.ntnu.idi.idatt.model;

public class Tile {
  private int number;
  private int x;
  private int y;
  private Tile snake; // The tile at the tail end of the snake
  private Tile ladder; // The tile at the top end of the ladder
  private Tile wormhole; // The tile at the destination of the wormhole

  /**
   * Default constructor
   */
  public Tile() {
  }

  /**
   * Constructor with tile number
   * 
   * @param number The tile number
   */
  public Tile(int number) {
    this.number = number;
  }

  /**
   * Gets the tile number
   * 
   * @return The tile number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Sets the tile number
   * 
   * @param number The tile number
   */
  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * Gets the x-coordinate of the tile
   * 
   * @return x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x-coordinate of the tile
   * 
   * @param x x-coordinate
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the y-coordinate of the tile
   * 
   * @return y-coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y-coordinate of the tile
   * 
   * @param y y-coordinate
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Sets the tile that this snake leads to (tail of the snake)
   * 
   * @param snake The tile at the tail end of the snake
   */
  public void setSnake(Tile snake) {
    this.snake = snake;
  }

  /**
   * Sets the tile that this ladder leads to (top of the ladder)
   * 
   * @param ladder The tile at the top end of the ladder
   */
  public void setLadder(Tile ladder) {
    this.ladder = ladder;
  }

  /**
   * Sets the tile that this wormhole leads to (destination of the wormhole)
   * 
   * @param wormhole The tile at the destination of the wormhole
   */
  public void setWormhole(Tile wormhole) {
    this.wormhole = wormhole;
  }

  /**
   * Gets the tile that the snake on this tile leads to
   * 
   * @return The tile at the end of the snake, or null if no snake
   */
  public Tile getSnake() {
    return snake;
  }

  /**
   * Gets the tile that the ladder on this tile leads to
   * 
   * @return The tile at the top of the ladder, or null if no ladder
   */
  public Tile getLadder() {
    return ladder;
  }

  /**
   * Gets the tile that the wormhole on this tile leads to
   * 
   * @return The tile at the destination of the wormhole, or null if no wormhole
   */
  public Tile getWormhole() {
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