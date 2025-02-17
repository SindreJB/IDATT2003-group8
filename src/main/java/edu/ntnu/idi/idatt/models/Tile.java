package edu.ntnu.idi.idatt.models;

public class Tile {
  private final int tileId;
  private TileAction landAction;
  private boolean hasAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public Tile(int tileId, TileAction action) {
    this.tileId = tileId;
    this.landAction = action;
    this.hasAction = false;
  }

  public int getTileId() {
    return tileId;
  }
  
  public void setAction(TileAction action) {
    this.landAction = action;
    this.hasAction = true;
  }

  public boolean hasAction() {
    return this.hasAction;
  }

  public TileAction getLandAction() {
    return landAction;
  }

}
