package edu.ntnu.idi.idatt.models;

public class Player {
  private final String name;
  private Tile currentTile;

  public Player(String name, Tile tile) {
    this.name = name;
    this.currentTile = tile; // Assuming players start at tile 1
  }

  public String getName() {
    return name;
  }

  public Integer getCurrentTileId() {
    return currentTile.getTileId();
  }

  public Tile getCurrentTile() {
    return this.currentTile;
  }

  public void setCurrentTile(Tile tile) {
      currentTile = tile;
  }
}