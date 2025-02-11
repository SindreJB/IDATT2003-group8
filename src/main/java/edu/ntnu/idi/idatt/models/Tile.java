package edu.ntnu.idi.idatt.models;

public class Tile {
  private Tile nextTile;
  private int tileId;
  private TileAction landAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public Tile(int tileId, TileAction action) {
    this.tileId = tileId;
    this.landAction = action;
  }

  public int getTile() {
    return tileId;
  }
  public TileAction getLandAction() {
    return landAction;
  }

  public void landPlayer(Player player) {
    if (landAction != null) {
      landAction.applyAction(player);
    }
  }

  public void leavePlayer(Player player) {}

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }
}
