package edu.ntnu.idi.idatt.models;

public class Player {
  private final String name;
  private int currentTile;

  public Player(String name) {
    this.name = name;
    this.currentTile = 1; // Assuming players start at tile 1
  }

  public String getName() {
    return name;
  }

  public Integer getCurrentTile() {
    return currentTile;
  }

  public void setCurrentTile(int tile) {
      currentTile = tile;
  }

  public void move(int roll) {
    currentTile += roll;
    if (currentTile > 90) {
      currentTile = 90; // Ensure the player does not move beyond the last tile
    }
  }
}