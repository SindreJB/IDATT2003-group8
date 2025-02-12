package edu.ntnu.idi.idatt.models;

public class LadderAction implements TileAction {
  private final int endTile;

  public LadderAction(int endTile) {
      this.endTile = endTile;
  }

  @Override
  public int tileActionResult() {
      return this.endTile;
  }

}
