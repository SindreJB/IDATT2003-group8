package edu.ntnu.idi.idatt.model.actions;

/**
 * Represents a snake that moves a player down the board.
 */
public class SnakeAction extends TileAbstract {

  /**
   * Creates a new snake action.
   * 
   * @param headTile The tile at the head of the snake
   * @param tailTile The tile at the tail of the snake
   */
  public SnakeAction(int headTile, int tailTile) {
    super(headTile, tailTile);
    if (tailTile >= headTile) {
      throw new IllegalArgumentException("Tail tile must be lower than head tile for a snake");
    }
  }

  @Override
  public String getActionType() {
    return "SNAKE";
  }

  @Override
  public String getDescription() {
    return "Slide down snake from " + getSourceTile() + " to " + getDestinationTile();
  }
}