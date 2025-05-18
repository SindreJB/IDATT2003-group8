package edu.ntnu.idi.idatt.model.actions;

import java.util.Random;

import edu.ntnu.idi.idatt.exceptions.ActionException;
import edu.ntnu.idi.idatt.model.Player;

/**
 * Represents a wormhole that randomly teleports a player.
 */
public class WormholeAction implements TileAction {
  private static final int MIN_MOVEMENT = -15; // Can move backward up to 15 spaces
  private static final int MAX_MOVEMENT = 20; // Can move forward up to 20 spaces

  private final int sourceTile;
  private final Random random;
  private int destinationTile; // Will be set randomly when performed

  /**
   * Creates a new wormhole action.
   * 
   * @param sourceTile The tile where the wormhole is located
   */
  public WormholeAction(int sourceTile) {
    this.sourceTile = sourceTile;
    this.random = new Random();
    this.destinationTile = -1; // Not yet determined
  }

  /**
   * Creates a new wormhole action with a predetermined destination.
   * 
   * @param sourceTile      The tile where the wormhole is located
   * @param destinationTile The predetermined destination tile
   */
  public WormholeAction(int sourceTile, int destinationTile) {
    this.sourceTile = sourceTile;
    this.destinationTile = destinationTile;
    this.random = null; // Not used when destination is predetermined
  }

  @Override
  public void perform(Player player) throws ActionException {
    if (player == null) {
      throw new ActionException("Cannot perform action on null player");
    }

    if (destinationTile == -1) {
      // Calculate random movement
      int randomMovement = random.nextInt(MAX_MOVEMENT - MIN_MOVEMENT + 1) + MIN_MOVEMENT;

      // Calculate new position (ensuring it's at least tile 1)
      destinationTile = Math.max(1, sourceTile + randomMovement);
    }

    // Move player to destination
    player.setTileId(destinationTile);
  }

  @Override
  public int getDestinationTile() {
    // If destination hasn't been determined yet, estimate the middle of the range
    if (destinationTile == -1) {
      return sourceTile + (MAX_MOVEMENT + MIN_MOVEMENT) / 2;
    }
    return destinationTile;
  }

  @Override
  public int getSourceTile() {
    return sourceTile;
  }

  @Override
  public String getActionType() {
    return "WORMHOLE";
  }

  @Override
  public String getDescription() {
    if (destinationTile == -1) {
      return "Teleport through wormhole from " + sourceTile + " to an unknown destination";
    }

    int movement = destinationTile - sourceTile;
    if (movement > 0) {
      return "Teleport forward " + movement + " spaces through wormhole";
    } else if (movement < 0) {
      return "Teleport backward " + Math.abs(movement) + " spaces through wormhole";
    } else {
      return "Enter wormhole but emerge in the same place";
    }
  }
}
