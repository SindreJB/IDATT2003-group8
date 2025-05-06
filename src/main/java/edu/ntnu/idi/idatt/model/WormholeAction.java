package edu.ntnu.idi.idatt.model;

import java.util.Objects;

/**
 * Represents a wormhole action that teleports a player to a different tile.
 */
public class WormholeAction implements TileAction {
  private int destinationTileNumber;
  private String description;
  private static final String TYPE = "Wormhole";

  /**
   * Creates a new wormhole action.
   *
   * @param startTileId           The ID of the tile where the wormhole starts
   * @param destinationTileNumber The ID of the tile where the wormhole leads to
   */
  public WormholeAction(int startTileId, int destinationTileNumber) {
    this.destinationTileNumber = destinationTileNumber;
    this.description = String.format("Wormhole from %d to %d",
        startTileId,
        destinationTileNumber);
  }

  @Override
  public void perform(Player player) {
    player.setTileId(destinationTileNumber);
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
  public int getDestinationTileNumber() {
    return destinationTileNumber;
  }

  public void setDestinationTileNumber(int destinationTileNumber) {
    this.destinationTileNumber = destinationTileNumber;
    // Update description if the destination changes
    if (description != null && description.contains("to")) {
      String[] parts = description.split("to");
      if (parts.length > 0) {
        this.description = parts[0] + "to " + destinationTileNumber;
      }
    }
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
    WormholeAction that = (WormholeAction) o;
    return destinationTileNumber == that.destinationTileNumber &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destinationTileNumber, description);
  }
}
