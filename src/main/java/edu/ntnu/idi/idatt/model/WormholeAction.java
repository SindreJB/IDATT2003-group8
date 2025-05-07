package edu.ntnu.idi.idatt.model;

import java.util.Objects;
import java.util.Random;

/**
 * Represents a wormhole action that teleports a player to a random position.
 * Wormholes can send players a random number of tiles forward or backward.
 */
public class WormholeAction implements TileAction {
  private int startTileId;
  private int destinationTileId; // Needed for compatibility with board loading
  private String description;
  private static final String TYPE = "Wormhole";
  private Random random = new Random();

  // Constants for random movement range
  private static final int MIN_MOVEMENT = -15; // Maximum backward movement
  private static final int MAX_MOVEMENT = 20; // Maximum forward movement

  /**
   * Creates a new wormhole action.
   *
   * @param startTileId       The ID of the tile where the wormhole starts
   * @param destinationTileId The ID of the tile where the wormhole leads to (used
   *                          for display only)
   */
  public WormholeAction(int startTileId, int destinationTileId) {
    this.startTileId = startTileId;
    this.destinationTileId = destinationTileId; // Store for compatibility
    this.description = String.format("Wormhole from %d - Moves randomly forward or backward!",
        startTileId);
  }

  @Override
  public void perform(Player player) {
    // Get current position and board size
    int currentPos = player.getTileId();

    // Generate random movement (-15 to +20 tiles)
    int randomMovement = random.nextInt(MAX_MOVEMENT - MIN_MOVEMENT + 1) + MIN_MOVEMENT;

    // Calculate new position and ensure it's valid (minimum position is 1)
    int newPosition = Math.max(1, currentPos + randomMovement);

    // Move player to new position
    player.setTileId(newPosition);
  }

  /**
   * Gets a description of what happened after the wormhole was activated
   * 
   * @param fromPosition The position before the wormhole
   * @param toPosition   The position after the wormhole
   * @return A descriptive message about what the wormhole did
   */
  public String getResultDescription(int fromPosition, int toPosition) {
    int difference = toPosition - fromPosition;

    if (difference > 0) {
      return String.format("The wormhole sent you forward %d spaces to %d!",
          difference, toPosition);
    } else if (difference < 0) {
      return String.format("The wormhole sent you backward %d spaces to %d!",
          Math.abs(difference), toPosition);
    } else {
      return "The wormhole spun you around but left you in the same place!";
    }
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  // Getters and setters for JSON serialization
  public int getStartTileId() {
    return startTileId;
  }

  public void setStartTileId(int startTileId) {
    this.startTileId = startTileId;
    updateDescription();
  }

  /**
   * Gets the destination tile ID (for compatibility with board loading)
   * Note: This doesn't affect the random movement behavior
   * 
   * @return the destination tile ID
   */
  public int getDestinationTileId() {
    return destinationTileId;
  }

  /**
   * Sets the destination tile ID (for compatibility with board loading)
   * Note: This doesn't affect the random movement behavior
   * 
   * @param destinationTileId the destination tile ID to set
   */
  public void setDestinationTileId(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Updates the description when start changes
   */
  private void updateDescription() {
    this.description = String.format("Wormhole from %d - Moves randomly forward or backward!",
        startTileId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WormholeAction that = (WormholeAction) o;
    return startTileId == that.startTileId &&
        destinationTileId == that.destinationTileId &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTileId, destinationTileId, description);
  }
}
