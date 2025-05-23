package edu.ntnu.idi.idatt.model;

public class TreasureGameTile extends Tile {
  private int tileType;
  private boolean hasTreasure;

  public TreasureGameTile(int number) {
    super(number);
    this.tileType = 0; 
    this.hasTreasure = false; 
  }

  public void setTileType(int tileType) {
    this.tileType = tileType;
  }

  public int getTileType() {
    return tileType;
  }
  
  public void setHasTreasure(boolean hasTreasure) {
    this.hasTreasure = hasTreasure;
  }
  
  public boolean hasTreasure() {
    return hasTreasure;
  }
}
