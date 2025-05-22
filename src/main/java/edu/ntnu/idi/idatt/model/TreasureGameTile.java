package edu.ntnu.idi.idatt.model;

public class TreasureGameTile extends Tile {
  private int tileType;

  public TreasureGameTile(int number) {
    super(number);
    this.tileType = 0; // Default value
  }

  public void setTileType(int tileType) {
    this.tileType = tileType;
  }

  public int getTileType() {
    return tileType;
  }
}
