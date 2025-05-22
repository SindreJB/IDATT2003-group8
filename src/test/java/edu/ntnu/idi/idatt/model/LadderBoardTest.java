package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LadderBoardTest {

  private LadderBoard standardBoard;

  @BeforeEach
  void setUp() {
    standardBoard = new LadderBoard(10, 10);
  }

  // POSITIVE TESTS

  @Test
  void constructorShouldCreateBoardWithSpecifiedDimensions() {
    assertEquals(10, standardBoard.getRows(), "Board should have 10 rows");
    assertEquals(10, standardBoard.getColumns(), "Board should have 10 columns");
    assertEquals(100, standardBoard.getTiles().size(), "Board should have 100 tiles");
  }

  @ParameterizedTest
  @CsvSource({
      "3, 3, 9",
      "5, 4, 20",
      "10, 10, 100"
  })
  void constructorShouldCreateCorrectNumberOfTiles(int rows, int columns, int expectedTiles) {
    LadderBoard board = new LadderBoard(rows, columns);
    assertEquals(expectedTiles, board.getTiles().size(),
        "Board should have rows*columns number of tiles");
  }

  @Test
  void tilesShouldBeArrangedInSnakePattern() {
    // Create a small board for easier verification
    LadderBoard board = new LadderBoard(3, 3);

    // First row: left to right (1,2,3)
    assertEquals(1, board.getTile(1).getNumber());
    assertEquals(2, board.getTile(2).getNumber());
    assertEquals(3, board.getTile(3).getNumber());

    // Second row: right to left (6,5,4)
    assertEquals(6, board.getTile(6).getNumber());
    assertEquals(5, board.getTile(5).getNumber());
    assertEquals(4, board.getTile(4).getNumber());

    // Third row: left to right (7,8,9)
    assertEquals(7, board.getTile(7).getNumber());
    assertEquals(8, board.getTile(8).getNumber());
    assertEquals(9, board.getTile(9).getNumber());
  }

  @Test
  void getTileShouldReturnCorrectLadderGameTile() {
    LadderGameTile tile = standardBoard.getTile(42);
    assertNotNull(tile, "getTile should return a tile for valid number");
    assertEquals(42, tile.getNumber(), "Returned tile should have correct number");
    assertTrue(tile instanceof LadderGameTile, "Returned tile should be LadderGameTile");
  }

  @Test
  void getGameBoardShouldReturnSameInstance() {
    assertSame(standardBoard, standardBoard.getGameBoard(),
        "getGameBoard should return the same board instance");
  }

  @Test
  void getRowsAndColumnsShouldReturnCorrectValues() {
    LadderBoard customBoard = new LadderBoard(6, 8);
    assertEquals(6, customBoard.getRows(), "getRows should return correct value");
    assertEquals(8, customBoard.getColumns(), "getColumns should return correct value");
  }

  // NEGATIVE TESTS

  @Test
  void getTileShouldHandleInvalidTileNumber() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> standardBoard.getTile(101),
        "getTile should throw IllegalArgumentException for tile number > total tiles");
    assertTrue(exception.getMessage().contains("tile number"),
        "Exception message should mention tile number");
  }

  @Test
  void getTileShouldHandleNegativeTileNumber() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> standardBoard.getTile(-5),
        "getTile should throw IllegalArgumentException for negative tile number");
    assertTrue(exception.getMessage().contains("tile number"),
        "Exception message should mention tile number");
  }

  @Test
  void getTileShouldHandleZeroTileNumber() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> standardBoard.getTile(0),
        "getTile should throw IllegalArgumentException for zero tile number");
    assertTrue(exception.getMessage().contains("tile number"),
        "Exception message should mention tile number");
  }

  @Test
  void largeBoardShouldInitializeCorrectly() {
    LadderBoard largeBoard = new LadderBoard(20, 20);
    assertEquals(400, largeBoard.getTiles().size(),
        "Large board should initialize all tiles correctly");
    assertNotNull(largeBoard.getTile(400),
        "Should be able to access last tile of large board");
    assertEquals(400, largeBoard.getTile(400).getNumber(),
        "Last tile should have correct number");
  }
}
