package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TreasureBoardConfigTest {

  private TreasureBoardConfig config;

  @BeforeEach
  public void setUp() {
    config = new TreasureBoardConfig();
  }

  // POSITIVE TESTS

  @Test
  void constructorShouldInitializeStandardLayout() {
    assertNotNull(config.STANDARD_LAYOUT, "Standard layout should be initialized");
    assertEquals(10, config.STANDARD_LAYOUT.length, "Standard layout should have 10 rows");
    assertEquals(10, config.STANDARD_LAYOUT[0].length, "Standard layout should have 10 columns per row");
  }

  @Test
  void rowsAndColumnsShouldBeCorrect() {
    assertEquals(10, config.ROWS, "ROWS constant should be 10");
    assertEquals(10, config.COLUMNS, "COLUMNS constant should be 10");
  }

  @Test
  void getTileTypeShouldReturnCorrectType() {
    // First check the actual values in the standard layout
    int startPosition = config.findStartPosition();
    
    // Test the start position (type 3)
    assertEquals(3, config.getTileType(startPosition), "Start position should be type 3");
    
    // Find and test a path tile (type 1), a treasure tile (type 2), and a void tile (type 0)
    boolean foundPath = false;
    boolean foundTreasure = false;
    boolean foundVoid = false;
    int pathTile = 0;
    int treasureTile = 0;
    int voidTile = 0;
    
    for (int i = 1; i <= 100; i++) {
      int type = config.getTileType(i);
      if (type == 1 && !foundPath) {
        foundPath = true;
        pathTile = i;
      } else if (type == 2 && !foundTreasure) {
        foundTreasure = true;
        treasureTile = i;
      } else if (type == 0 && !foundVoid) {
        foundVoid = true;
        voidTile = i;
      }
      
      if (foundPath && foundTreasure && foundVoid) break;
    }
    
    // Test a path tile (type 1)
    assertTrue(foundPath, "Should find at least one path tile");
    assertEquals(1, config.getTileType(pathTile), "Path tile should be type 1");
    
    // Test a treasure tile (type 2)
    assertTrue(foundTreasure, "Should find at least one treasure tile");
    assertEquals(2, config.getTileType(treasureTile), "Treasure tile should be type 2");
    
    // Test a void tile (type 0)
    assertTrue(foundVoid, "Should find at least one void tile");
    assertEquals(0, config.getTileType(voidTile), "Void tile should be type 0");
  }

  @Test
  void isWalkableShouldReturnTrueForWalkableTiles() {
    // Find and test tiles of each walkable type
    int pathTile = findTileOfType(1);
    int treasureTile = findTileOfType(2);
    int startTile = config.findStartPosition();
    
    // Test a path tile (type 1)
    assertTrue(config.isWalkable(pathTile), "Path tile should be walkable");
    
    // Test a treasure tile (type 2)
    assertTrue(config.isWalkable(treasureTile), "Treasure tile should be walkable");
    
    // Test start position (type 3)
    assertTrue(config.isWalkable(startTile), "Start position should be walkable");
  }
  
  // Helper method to find a tile of a specific type
  private int findTileOfType(int type) {
    for (int i = 1; i <= 100; i++) {
      if (config.getTileType(i) == type) {
        return i;
      }
    }
    return -1; // Not found
  }

  @Test
  void isWalkableShouldReturnFalseForNonWalkableTiles() {
    // Test a void tile (type 0)
    assertFalse(config.isWalkable(91), "Void tile should not be walkable");
  }

  @Test
  void findStartPositionShouldReturnCorrectTile() {
    assertEquals(95, config.findStartPosition(), "Start position should be tile 95");
  }

  @Test
  void getCoordinatesShouldReturnCorrectValues() {
    // Test tile 95 (start position at row 9, col 4)
    int[] coords = config.getCoordinates(95);
    assertNotNull(coords, "Coordinates should not be null");
    assertEquals(2, coords.length, "Coordinates array should have 2 elements");
    assertEquals(9, coords[0], "Row should be 9");
    assertEquals(4, coords[1], "Column should be 4");
    
    // Test tile 1 (top-left at row 0, col 0)
    coords = config.getCoordinates(1);
    assertNotNull(coords, "Coordinates should not be null");
    assertEquals(0, coords[0], "Row should be 0");
    assertEquals(0, coords[1], "Column should be 0");
    
    // Test tile 100 (bottom-right at row 9, col 9)
    coords = config.getCoordinates(100);
    assertNotNull(coords, "Coordinates should not be null");
    assertEquals(9, coords[0], "Row should be 9");
    assertEquals(9, coords[1], "Column should be 9");
  }

  @Test
  void getTileIdShouldReturnCorrectId() {
    // Test start position coordinates (row 9, col 4)
    assertEquals(95, config.getTileId(9, 4), "Tile ID for row 9, col 4 should be 95");
    
    // Test top-left coordinates (row 0, col 0)
    assertEquals(1, config.getTileId(0, 0), "Tile ID for row 0, col 0 should be 1");
    
    // Test bottom-right coordinates (row 9, col 9)
    assertEquals(100, config.getTileId(9, 9), "Tile ID for row 9, col 9 should be 100");
  }

  // NEGATIVE TESTS

  @Test
  void getTileTypeShouldHandleInvalidTileIds() {
    assertEquals(0, config.getTileType(-1), "Should return 0 for negative tile ID");
    assertEquals(0, config.getTileType(0), "Should return 0 for tile ID 0");
    assertEquals(0, config.getTileType(101), "Should return 0 for tile ID > 100");
  }

  @Test
  void isWalkableShouldHandleInvalidTileIds() {
    assertFalse(config.isWalkable(-1), "Should return false for negative tile ID");
    assertFalse(config.isWalkable(0), "Should return false for tile ID 0");
    assertFalse(config.isWalkable(101), "Should return false for tile ID > 100");
  }

  @Test
  void getCoordinatesShouldHandleInvalidTileIds() {
    assertNull(config.getCoordinates(-1), "Should return null for negative tile ID");
    assertNull(config.getCoordinates(0), "Should return null for tile ID 0");
    assertNull(config.getCoordinates(101), "Should return null for tile ID > 100");
  }

  @Test
  void getTileIdShouldHandleInvalidCoordinates() {
    assertEquals(-1, config.getTileId(-1, 0), "Should return -1 for negative row");
    assertEquals(-1, config.getTileId(0, -1), "Should return -1 for negative column");
    assertEquals(-1, config.getTileId(10, 0), "Should return -1 for row >= ROWS");
    assertEquals(-1, config.getTileId(0, 10), "Should return -1 for column >= COLUMNS");
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3})
  void standardLayoutShouldOnlyContainValidTileTypes(int tileType) {
    boolean containsType = false;
    for (int i = 0; i < config.ROWS; i++) {
      for (int j = 0; j < config.COLUMNS; j++) {
        if (config.STANDARD_LAYOUT[i][j] == tileType) {
          containsType = true;
          break;
        }
      }
      if (containsType) break;
    }
    assertTrue(containsType, "Standard layout should contain tile type " + tileType);
  }
}
