package edu.ntnu.idi.idatt.model;

public class Tile {
  private int number;
  private int x;
  private int y;
  private Tile snake; // The tile at the tail end of the snake
  private Tile ladder; // The tile at the top end of the ladder

  // Existing constructor and methods...

  /**
   * Sets the tile that this snake leads to (tail of the snake)
   * @param snake The tile at the tail end of the snake
   */
  public void setSnake(Tile snake) {
    this.snake = snake;
  }

  /**
   * Sets the tile that this ladder leads to (top of the ladder)
   * @param ladder The tile at the top end of the ladder
   */
  public void setLadder(Tile ladder) {
    this.ladder = ladder;
  }

  /**
   * Gets the tile that the snake on this tile leads to
   * @return The tile at the end of the snake, or null if no snake
   */
  public Tile getSnake() {
    return snake;
  }

  /**
   * Gets the tile that the ladder on this tile leads to
   * @return The tile at the top of the ladder, or null if no ladder
   */
  public Tile getLadder() {
    return ladder;
  }

  /**
   * Checks if this tile has a snake
   * @return true if this tile has a snake, false otherwise
   */
  public boolean hasSnake() {
    return snake != null;
  }

  /**
   * Checks if this tile has a ladder
   * @return true if this tile has a ladder, false otherwise
   */
  public boolean hasLadder() {
    return ladder != null;
  }

  /**
   * Gets the tile number
   * @return The tile number
   */
  public int getNumber() {
    return number;
  }
}