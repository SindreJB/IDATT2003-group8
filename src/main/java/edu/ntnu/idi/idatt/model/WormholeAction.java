package edu.ntnu.idi.idatt.model;

public class WormholeAction implements TileAction {
  private int destinationTileNumber;

  public WormholeAction(int destinationTileNumber) {
    this.destinationTileNumber = destinationTileNumber;
  }

  public int getDestinationTileNumber() {
    return destinationTileNumber;
  }

  public void setDestinationTileNumber(int destinationTileNumber) {
    this.destinationTileNumber = destinationTileNumber;
  }

  @Override
  public String toString() {
    return "Wormhole to tile " + destinationTileNumber;
  }

  @Override
  public String getType() {
    return "Wormhole";
  }

  @Override
  public void perform(Player player) {
    player.setTileId(destinationTileNumber);
  }

  @Override
  public String getDescription() {
    return "Teleports player to tile " + destinationTileNumber;
  }
}
