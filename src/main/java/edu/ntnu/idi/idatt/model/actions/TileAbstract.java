package edu.ntnu.idi.idatt.model.actions;

import edu.ntnu.idi.idatt.exceptions.ActionException;
import edu.ntnu.idi.idatt.model.Player;

/**
 * Abstract base class for tile actions that provides common functionality.
 */
public abstract class TileAbstract implements TileAction {
  private final int sourceTile;
  private final int destinationTile;

  /**
   * Creates a new tile action.
   * 
   * @param sourceTile      The tile where the action starts
   * @param destinationTile The tile where the action ends
   */
  public TileAbstract(int sourceTile, int destinationTile) {
    this.sourceTile = sourceTile;
    this.destinationTile = destinationTile;
  }

  @Override
  public int getDestinationTile() {
    return destinationTile;
  }

  @Override
  public int getSourceTile() {
    return sourceTile;
  }

  @Override
  public void perform(Player player) throws ActionException {
    if (player == null) {
      throw new ActionException("Cannot perform action on null player");
    }

    player.setTileId(destinationTile);
  }
}