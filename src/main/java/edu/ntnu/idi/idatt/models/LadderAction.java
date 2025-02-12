package edu.ntnu.idi.idatt.models;

public class LadderAction implements TileAction {
  private final Tile endTile;

  public LadderAction(Tile endTile) {
      this.endTile = endTile;
  }

  @Override
  public Tile tileActionResult() {
      return this.endTile;
  }

}
