package edu.ntnu.idi.idatt.model;

/**
 * The SnakeAction class represents an action that moves a player to a different
 * tile
 * when they land on a snake in a board game. This class implements the
 * TileAction interface.
 */
public class SnakeAction implements TileAction {
  private final int endTile;

  /**
   * Represents an action taken by a snake in a game, which involves moving to a
   * specific end tile.
   *
   * @param endTile the tile number where the snake action ends
   */
  public SnakeAction(int endTile) {
    this.endTile = endTile;
  }

  /**
   * Returns the result of the tile action.
   *
   * @return the end tile value.
   */
  @Override
  public int tileActionResult() {
    return this.endTile;
  }

}
