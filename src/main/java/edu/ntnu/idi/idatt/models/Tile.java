package edu.ntnu.idi.idatt.models;

public class Tile {
  private Tile nextTile;
  private int tileId;
  private TileAction landAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public int getTileId() {
    return tileId;
  }
  public void landPlayer(Player player) {}
  public void leavePlayer(Player player) {}
  public void setNextTile(Tile nextTile) {}
}
