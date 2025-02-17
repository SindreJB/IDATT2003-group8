package edu.ntnu.idi.idatt.models;

public class Player {
  private final String name;
  private int tileId;

  public Player(String name, int tileId) {
    this.name = name;
    this.tileId = tileId; 
  }

  public String getName() {
    return name;
  }

  public Integer getTileId() {
    return this.tileId;
  }


  public void setTileId(int tileId) {
      this.tileId = tileId;
  }
}