package edu.ntnu.idi.idatt.models;

public class LadderAction implements TileAction {
  private final int endTile;

  public LadderAction(int endTile) {
      this.endTile = endTile;
  }

  @Override
  public void applyAction(Player player) {
      player.setCurrentTile(endTile);
  }

}
