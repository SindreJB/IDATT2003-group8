package edu.ntnu.idi.idatt.model;

public abstract class Tile {
  private int number;

  

  public Tile(int number) {
    this.number = number;
  }

  /**
   * Gets the tile number
   * 
   * @return The tile number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Sets the tile number
   * 
   * @param number The tile number
   */
  public void setNumber(int number) {
    this.number = number;
  }
}
