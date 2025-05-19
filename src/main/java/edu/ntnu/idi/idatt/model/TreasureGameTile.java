package edu.ntnu.idi.idatt.model;

public class TreasureGameTile extends Tile {
  private int treasureValue;

  public TreasureGameTile(int number) {
    super(number);
    this.treasureValue = 0; // Default value
  }

  public int getTreasureValue() {
    return treasureValue;
  }

  public void setTreasureValue(int treasureValue) {
    this.treasureValue = treasureValue;
  }

  @Override
  public String toString() {
    return "TreasureGameTile{" +
        "number=" + getNumber() +
        ", treasureValue=" + treasureValue +
        '}';
  }

}
