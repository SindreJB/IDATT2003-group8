package edu.ntnu.idi.idatt.model.actions;

/**
 * Represents a ladder that moves a player up the board.
 */
public class LadderAction extends TileAbstract {

  /**
   * Creates a new ladder action.
   * 
   * @param bottomTile The tile at the bottom of the ladder
   * @param topTile    The tile at the top of the ladder
   */
  public LadderAction(int bottomTile, int topTile) {
    super(bottomTile, topTile);
    if (topTile <= bottomTile) {
      throw new IllegalArgumentException("Top tile must be higher than bottom tile for a ladder");
    }
  }

  @Override
  public String getActionType() {
    return "LADDER";
  }

  @Override
  public String getDescription() {
    return "Climb ladder from " + getSourceTile() + " to " + getDestinationTile();
  }
}