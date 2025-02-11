package edu.ntnu.idi.idatt.models;

public class Tile {
  private Tile nextTile;
  private int tileId;
  private TileAction tileAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public Tile(int tileId, TileAction action) {
    this.tileId = tileId;
    this.tileAction = action;
  }

  public int getTileId() {
    return tileId;
  }
  public void landPlayer(Player player) {}
  public void leavePlayer(Player player) {}
  public void setNextTile(Tile nextTile) {}
}
