package edu.ntnu.idi.idatt.models;

public class JailAction implements TileAction {
  private final int endTile;

  public JailAction(int endtTile) {
    this.endTile = endtTile;
  } 
  
  @Override
    public int tileActionResult() {
      return endTile;
    }
  
}
 