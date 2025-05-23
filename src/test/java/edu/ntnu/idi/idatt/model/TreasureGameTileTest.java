package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreasureGameTileTest {

  private TreasureGameTile tile;

  @BeforeEach
  public void setUp() {
    tile = new TreasureGameTile(10);
  }

  // POSITIVE TESTS

  @Test
  public void constructorShouldSetTileNumber() {
    assertEquals(10, tile.getNumber(), "Tile number should be properly set");
  }

  @Test
  public void constructorShouldInitializeWithDefaultValues() {
    assertEquals(0, tile.getTileType(), "Tile type should default to 0");
    assertFalse(tile.hasTreasure(), "New tile should not have treasure by default");
  }

  @Test
  public void setTileTypeShouldUpdateTileType() {
    // Set tile type to path (1)
    tile.setTileType(1);
    assertEquals(1, tile.getTileType(), "Tile type should be updated to 1");
    
    // Set tile type to treasure location (2)
    tile.setTileType(2);
    assertEquals(2, tile.getTileType(), "Tile type should be updated to 2");
  }

  @Test
  public void setHasTreasureShouldUpdateTreasureStatus() {
    // Set treasure to true
    tile.setHasTreasure(true);
    assertTrue(tile.hasTreasure(), "Tile should have treasure after setting to true");
    
    // Set treasure to false
    tile.setHasTreasure(false);
    assertFalse(tile.hasTreasure(), "Tile should not have treasure after setting to false");
  }
  
  @Test
  public void tileTypeAndTreasureStatusShouldBeIndependent() {
    // Set tile type to treasure location (2) but no actual treasure
    tile.setTileType(2);
    tile.setHasTreasure(false);
    
    assertEquals(2, tile.getTileType(), "Tile type should be 2");
    assertFalse(tile.hasTreasure(), "Tile should not have treasure");
    
    // Set tile type to regular path (1) but with a treasure
    tile.setTileType(1);
    tile.setHasTreasure(true);
    
    assertEquals(1, tile.getTileType(), "Tile type should be 1");
    assertTrue(tile.hasTreasure(), "Tile should have treasure");
  }

  // NEGATIVE TESTS
  
  @Test
  public void setTileTypeShouldHandleNegativeValues() {
    tile.setTileType(-1);
    assertEquals(-1, tile.getTileType(), "Tile type should accept negative values");
  }
  
  @Test
  public void setTileTypeShouldHandleLargeValues() {
    tile.setTileType(Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, tile.getTileType(), "Tile type should accept large values");
  }
  
  @Test
  public void multipleTilesShouldBeIndependent() {
    TreasureGameTile tile1 = new TreasureGameTile(1);
    TreasureGameTile tile2 = new TreasureGameTile(2);
    
    tile1.setTileType(2);
    tile1.setHasTreasure(true);
    
    assertEquals(0, tile2.getTileType(), "Tile2 type should remain unchanged");
    assertFalse(tile2.hasTreasure(), "Tile2 treasure status should remain unchanged");
  }
}
