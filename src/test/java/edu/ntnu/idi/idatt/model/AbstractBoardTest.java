package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractBoardTest {

  // Concrete implementation of AbstractBoard for testing
  private static class TestBoard extends AbstractBoard<TestTile> {
    private final int rows;
    private final int columns;

    public TestBoard(int rows, int columns) {
      super();
      this.rows = rows;
      this.columns = columns;
      initialize();
    }

    @Override
    protected void initializeTiles() {
      tiles.clear();
      for (int i = 0; i < rows * columns; i++) {
        tiles.add(new TestTile(i + 1));
      }
    }
    
    @SuppressWarnings("unused")
    public int getRows() {
      return rows;
    }
    
    @SuppressWarnings("unused")
    public int getColumns() {
      return columns;
    }
  }
  
  // Simple tile implementation for testing
  private static class TestTile extends Tile {
    public TestTile(int number) {
      super(number);
    }
  }
  
  private TestBoard board;
  
  @BeforeEach
  public void setUp() {
    board = new TestBoard(10, 10);
  }
  
  // POSITIVE TESTS
  
  @Test
  void constructorShouldInitializeBoard() {
    assertNotNull(board, "Board should be initialized");
    assertEquals(100, board.getNumberOfTiles(), "Board should have rows * columns tiles");
  }
  
  @Test
  void getTileShouldReturnCorrectTile() {
    TestTile tile = board.getTile(5);
    assertNotNull(tile, "Tile should not be null");
    assertEquals(5, tile.getNumber(), "Tile should have correct number");
  }
  
  @Test
  void getNumberOfTilesShouldReturnCorrectCount() {
    assertEquals(100, board.getNumberOfTiles(), "Board should report correct number of tiles");
    
    // Test smaller board
    TestBoard smallBoard = new TestBoard(3, 3);
    assertEquals(9, smallBoard.getNumberOfTiles(), "Small board should report correct number of tiles");
  }
  
  @Test
  void initializeShouldCreateCorrectTiles() {
    // Creating a new board will call initialize
    TestBoard newBoard = new TestBoard(5, 5);
    
    assertEquals(25, newBoard.getNumberOfTiles(), "Board should have correct number of tiles");
    
    // Check that all tiles have correct numbers
    for (int i = 1; i <= 25; i++) {
      TestTile tile = newBoard.getTile(i);
      assertEquals(i, tile.getNumber(), "Tile " + i + " should have correct number");
    }
  }
  
  // NEGATIVE TESTS
  
  @Test
  void getTileShouldThrowExceptionForInvalidNumber() {
    assertThrows(IllegalArgumentException.class, () -> board.getTile(-1),
                "Getting tile with negative number should throw IllegalArgumentException");
    
    assertThrows(IllegalArgumentException.class, () -> board.getTile(0),
                "Getting tile with number 0 should throw IllegalArgumentException");
    
    assertThrows(IllegalArgumentException.class, () -> board.getTile(101),
                "Getting tile with number > board size should throw IllegalArgumentException");
  }
  
  @Test
  void initializeShouldSupportZeroSizeBoard() {
    TestBoard emptyBoard = new TestBoard(0, 0);
    assertEquals(0, emptyBoard.getNumberOfTiles(), "Empty board should have 0 tiles");
  }
  
  @Test
  void validateTileNumberShouldThrowForInvalidTiles() {
    @SuppressWarnings("unused")
    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> board.getTile(0),
                "Validating tile 0 should throw IllegalArgumentException");
    @SuppressWarnings("unused")
    Exception exception2 = assertThrows(IllegalArgumentException.class, () -> board.getTile(-5),
                "Validating negative tile should throw IllegalArgumentException");
    @SuppressWarnings("unused")
    Exception exception3 = assertThrows(IllegalArgumentException.class, () -> board.getTile(101),
                "Validating tile > board size should throw IllegalArgumentException");
  }
}
