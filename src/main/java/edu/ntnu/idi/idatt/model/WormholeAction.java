package edu.ntnu.idi.idatt.model;

import java.util.Objects;
import java.util.Random;

/**
 * Represents a wormhole action that teleports a player to a different tile.
 * Wormholes can work both ways, providing bidirectional travel with a
 * probability-based mechanism.
 */
public class WormholeAction implements TileAction {
  private int destinationTileNumber;
  private int originTileNumber;
  private String description;
  private static final String TYPE = "Wormhole";
  private double bidirectionalProbability = 0.5; // 50% chance of reverse travel
  private Random random = new Random();

  /**
   * Creates a new wormhole action.
   *
   * @param startTileId           The ID of the tile where the wormhole starts
   * @param destinationTileNumber The ID of the tile where the wormhole leads to
   */
  public WormholeAction(int startTileId, int destinationTileNumber) {
    this.originTileNumber = startTileId;
    this.destinationTileNumber = destinationTileNumber;
    this.description = String.format("Bidirectional Wormhole: %d ⟷ %d",
        startTileId,
        destinationTileNumber);
  }

  @Override
  public void perform(Player player) {
    int currentPosition = player.getTileId();

    // Determine which way to travel based on player's current position
    if (currentPosition == originTileNumber) {
      // Player is at the origin - send to destination
      player.setTileId(destinationTileNumber);
    } else if (currentPosition == destinationTileNumber) {
      // Player is at the destination - send back to origin
      // with a probability determined by bidirectionalProbability
      if (random.nextDouble() < bidirectionalProbability) {
        player.setTileId(originTileNumber);
      }
      // Otherwise stay at destination (wormhole doesn't activate)
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

  /**
   * Sets the probability that the wormhole will work in reverse
   * (from destination back to origin)
   * 
   * @param probability A value between 0.0 (never) and 1.0 (always)
   */
  public void setBidirectionalProbability(double probability) {
    if (probability < 0.0 || probability > 1.0) {
      throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
    }
    this.bidirectionalProbability = probability;
  }

  /**
   * Gets the probability that the wormhole will work in reverse
   * 
   * @return The probability value between 0.0 and 1.0
   */
  public double getBidirectionalProbability() {
    return bidirectionalProbability;
  }

  // Getters and setters for JSON serialization
  public int getDestinationTileNumber() {
    return destinationTileNumber;
  }

  public void setDestinationTileNumber(int destinationTileNumber) {
    this.destinationTileNumber = destinationTileNumber;
    updateDescription();
  }

  public int getOriginTileNumber() {
    return originTileNumber;
  }

  public void setOriginTileNumber(int originTileNumber) {
    this.originTileNumber = originTileNumber;
    updateDescription();
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Updates the description when origin or destination changes
   */
  private void updateDescription() {
    this.description = String.format("Bidirectional Wormhole: %d ⟷ %d",
        originTileNumber,
        destinationTileNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WormholeAction that = (WormholeAction) o;
    return destinationTileNumber == that.destinationTileNumber &&
        originTileNumber == that.originTileNumber &&
        Double.compare(that.bidirectionalProbability, bidirectionalProbability) == 0 &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destinationTileNumber, originTileNumber,
        bidirectionalProbability, description);
  }
}
