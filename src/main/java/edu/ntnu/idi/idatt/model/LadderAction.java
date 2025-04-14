package edu.ntnu.idi.idatt.model;

import java.util.Objects;

/**
 * Represents a ladder or snake action that moves a player to a different tile.
 * Can be used for both upward movement (ladders) and downward movement
 * (snakes).
 */
public class LadderAction implements TileAction {
  private int destinationTileId;
  private String description;
  private static final String TYPE = "LadderAction";

  /**
   * Creates a new ladder action.
   *
   * @param startTileId       The ID of the tile where the ladder/snake starts
   * @param destinationTileId The ID of the tile where the ladder/snake ends
   */
  public LadderAction(int startTileId, int destinationTileId) {
    this.destinationTileId = destinationTileId;
    this.description = String.format(
        destinationTileId > startTileId ? "Ladder from %d to %d" : "Snake from %d to %d",
        startTileId,
        destinationTileId);
  }

  @Override
  public void perform(Player player) {
    player.setTileId(destinationTileId);
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
  public int getDestinationTileId() {
    return destinationTileId;
  }

  public void setDestinationTileId(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    LadderAction that = (LadderAction) o;
    return destinationTileId == that.destinationTileId &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destinationTileId, description);
  }
}