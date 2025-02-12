package edu.ntnu.idi.idatt.models;

public class SnakeAction implements TileAction {
  private final Tile endTile;

  public SnakeAction(Tile endTile) {
      this.endTile = endTile;
  }

  @Override
  public Tile tileActionResult() {
      return this.endTile;
  }

}
