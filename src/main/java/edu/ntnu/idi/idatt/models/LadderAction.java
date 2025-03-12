package edu.ntnu.idi.idatt.models;

/**
 * The LadderAction class represents an action that moves a player to a specific
 * tile
 * when they land on a ladder in a board game.
 * 
 * <p>
 * This class implements the TileAction interface and provides the
 * implementation
 * for the tileActionResult method, which returns the tile number where the
 * player
 * should be moved to.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * LadderAction ladderAction = new LadderAction(10);
 * int result = ladderAction.tileActionResult(); // result will be 10
 * }
 * </pre>
 * 
 * @author Your Name
 */
public class LadderAction implements TileAction {
  private final int endTile;

  /**
   * Represents an action involving a ladder in a game.
   * This action moves a player to a specified end tile.
   *
   * @param endTile the tile number where the ladder ends
   */
  public LadderAction(int endTile) {
    this.endTile = endTile;
  }

  /**
   * Executes the action associated with the tile and returns the result.
   *
   * @return the end tile position after the action is performed.
   */
  @Override
  public int tileActionResult() {
    return this.endTile;
  }

}
