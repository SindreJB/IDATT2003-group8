package edu.ntnu.idi.idatt.models;

public class SnakeAction implements TileAction {
  private final int endTile;

  public SnakeAction(int endTile) {
    this.endTile = endTile;
  }

  @Override
  public void applyAction(Player player) {
    player.setCurrentTile(endTile);
  }

}
