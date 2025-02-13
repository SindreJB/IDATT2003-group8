package edu.ntnu.idi.idatt.models;

public class Player {
  private final String name;
  private int tileId;
  private boolean jailed;

  public Player(String name, int tileId) {
    this.name = name;
    this.tileId = tileId; 
    this.jailed = false;
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

  public boolean getJailed() {
    return this.jailed;
  }

  public void setJailed(boolean jailed) {
    this.jailed = jailed;
  }
}