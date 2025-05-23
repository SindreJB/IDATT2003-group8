package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TreasureBoardTest {

  private TreasureBoard board;
  private final int ROWS = 10;
  private final int COLUMNS = 10;

  @BeforeEach
  public void setUp() {
    board = new TreasureBoard(ROWS, COLUMNS);
    board.initializeBoardWithTreasure();
  }

  // POSITIVE TESTS

  @Test
  void constructorShouldCreateBoardWithSpecifiedDimensions() {
    assertEquals(ROWS, board.getRows(), "Board should have specified number of rows");
    assertEquals(COLUMNS, board.getColumns(), "Board should have specified number of columns");
  }

  @Test
  void initializeBoardWithTreasureShouldCreateCorrectNumberOfTiles() {
    assertEquals(ROWS * COLUMNS, board.getNumberOfTiles(), "Board should have ROWS * COLUMNS tiles");
  }

  @Test
  void getTileShouldReturnCorrectTileType() {
    // Find and test a path tile (type 1)
    boolean foundPath = false;
    boolean foundTreasure = false;
    boolean foundStart = false;
    for (int i = 1; i <= ROWS * COLUMNS; i++) {
      TreasureGameTile tile = board.getTile(i);
      if (!foundPath && tile.getTileType() == 1) {
        assertEquals(1, tile.getTileType(), "Tile should be of type 1 (path)");
        foundPath = true;
      }
      if (!foundTreasure && tile.getTileType() == 2) {
        assertEquals(2, tile.getTileType(), "Tile should be of type 2 (treasure location)");
        foundTreasure = true;
      }
      if (!foundStart && tile.getTileType() == 3) {
        assertEquals(3, tile.getTileType(), "Tile should be of type 3 (start position)");
        foundStart = true;
      }
      if (foundPath && foundTreasure && foundStart) break;
    }
    assertTrue(foundPath, "Should find at least one path tile (type 1)");
    assertTrue(foundTreasure, "Should find at least one treasure location tile (type 2)");
    assertTrue(foundStart, "Should find at least one start position tile (type 3)");
  }

  @Test
  void assignRandomTreasureShouldPlaceTreasureOnType2Tile() {
    // Reset the board to ensure no treasure
    for (int i = 1; i <= ROWS * COLUMNS; i++) {
      TreasureGameTile tile = board.getTile(i);
      if (tile != null) {
        tile.setHasTreasure(false);
      }
    }
    
    // Assign a random treasure
    board.assignRandomTreasure();
    
    // Check that a treasure exists and is on a type 2 tile
    int treasureTileId = board.getTreasureTileId();
    assertTrue(treasureTileId > 0, "Treasure tile ID should be positive");
    
    TreasureGameTile treasureTile = board.getTile(treasureTileId);
    assertTrue(treasureTile.hasTreasure(), "Treasure tile should have treasure");
    assertEquals(2, treasureTile.getTileType(), "Treasure should be on a type 2 tile");
  }

  @Test
  void multipleTreasureAssignmentsShouldCreateDifferentPatterns() {
    // Store the initial treasure location
    int initialTreasureTileId = board.getTreasureTileId();
    
    // Reset and reassign multiple times
    boolean differentLocationFound = false;
    for (int i = 0; i < 10 && !differentLocationFound; i++) {
      // Reset all treasures
      for (int j = 1; j <= ROWS * COLUMNS; j++) {
        TreasureGameTile tile = board.getTile(j);
        if (tile != null) {
          tile.setHasTreasure(false);
        }
      }
      
      // Assign a new random treasure
      board.assignRandomTreasure();
      int newTreasureTileId = board.getTreasureTileId();
      
      if (newTreasureTileId != initialTreasureTileId) {
        differentLocationFound = true;
      }
    }
    
    assertTrue(differentLocationFound, "Multiple treasure assignments should eventually create different patterns");
  }

  @ParameterizedTest
  @CsvSource({
      "3, 3, 9",
      "5, 4, 20",
      "10, 10, 100"
  })
  void differentBoardSizesShouldCreateCorrectNumberOfTiles(int rows, int columns, int expectedTiles) {
    TreasureBoard customBoard = new TreasureBoard(rows, columns);
    customBoard.initializeBoardWithTreasure();
    assertEquals(expectedTiles, customBoard.getNumberOfTiles(), "Board should have rows * columns tiles");
  }

  // NEGATIVE TESTS

  @Test
  void getTileShouldHandleInvalidTileNumber() {
    assertThrows(IllegalArgumentException.class, () -> board.getTile(0), 
                "Getting tile 0 should throw IllegalArgumentException");
    assertThrows(IllegalArgumentException.class, () -> board.getTile(ROWS * COLUMNS + 1), 
                "Getting tile > ROWS * COLUMNS should throw IllegalArgumentException");
  }

  @Test
  void initializeBoardWithTreasureShouldWorkMultipleTimes() {
    // Initialize board multiple times
    board.initializeBoardWithTreasure();
    board.initializeBoardWithTreasure();
    
    // Verify board is still valid
    assertEquals(ROWS * COLUMNS, board.getNumberOfTiles(), "Board should maintain correct number of tiles");
    assertNotEquals(-1, board.getTreasureTileId(), "Board should have a treasure tile after multiple initializations");
  }

  @Test
  void assignRandomTreasureShouldHandleNoType2Tiles() {
    // Override the initializeTiles method through anonymous subclass
    TreasureBoard noTreasureBoard = new TreasureBoard(3, 3) {
      @Override
      protected void initializeTiles() {
        tiles.clear();
        for (int i = 0; i < 9; i++) {
          TreasureGameTile tile = new TreasureGameTile(i + 1);
          tile.setTileType(1); // All tiles are paths, no treasure locations
          tiles.add(tile);
        }
      }
    };
    
    noTreasureBoard.initializeTiles();
    noTreasureBoard.assignRandomTreasure();
    
    // Verify no treasure was assigned
    assertEquals(-1, noTreasureBoard.getTreasureTileId(), "Board without type 2 tiles should have no treasure");
  }

  @Test
  void boardShouldSupportZeroByZeroSize() {
    TreasureBoard emptyBoard = new TreasureBoard(0, 0);
    emptyBoard.initializeBoardWithTreasure();
    assertEquals(0, emptyBoard.getNumberOfTiles(), "Empty board should have 0 tiles");
    assertEquals(-1, emptyBoard.getTreasureTileId(), "Empty board should have no treasure");
  }
}
