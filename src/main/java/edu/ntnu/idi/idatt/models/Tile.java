package edu.ntnu.idi.idatt.models;

public class Tile {
  private final int tileId;
  private TileAction action;
  private boolean hasAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public Tile(int tileId, TileAction action) {
    this.tileId = tileId;
    this.action = action;
    this.hasAction = false;
  }

  public int getTileId() {
    return tileId;
  }
  
  public void setAction(TileAction action) {
    this.action = action;
    this.hasAction = true;
  }

  public boolean hasAction() {
    return this.hasAction;
  }

  public TileAction getAction() {
    return action;
  }

}
