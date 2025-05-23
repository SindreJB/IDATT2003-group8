package edu.ntnu.idi.idatt.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import edu.ntnu.idi.idatt.model.BoardConfig;
import edu.ntnu.idi.idatt.model.LadderBoard;

public class LadderGameFactoryTest {

  // POSITIVE TESTS

  @Test
  public void testCreateBoardFromConfig_BasicProperties() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(8);
    config.setColumns(7);
    config.setName("Test Board");
    config.setDescription("A test board for unit testing");

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertEquals("Test Board", board.getName(), "Board name should match config");
    assertEquals("A test board for unit testing", board.getDescription(), "Board description should match config");
    assertEquals(8, board.getRows(), "Board should have correct number of rows");
    assertEquals(7, board.getColumns(), "Board should have correct number of columns");
    assertEquals(56, board.getTiles().size(), "Board should have rows*columns tiles");
  }

  @Test
  public void testCreateBoardFromConfig_WithSnakes() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add snakes
    config.addSnake(15, 5); // Snake from tile 15 to tile 5
    config.addSnake(24, 16); // Snake from tile 24 to tile 16

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertTrue(board.getTile(15).hasSnake(), "Tile 15 should have a snake");
    assertEquals(5, board.getTile(15).getSnake().getNumber(), "Snake should lead to tile 5");

    assertTrue(board.getTile(24).hasSnake(), "Tile 24 should have a snake");
    assertEquals(16, board.getTile(24).getSnake().getNumber(), "Snake should lead to tile 16");
  }

  @Test
  public void testCreateBoardFromConfig_WithLadders() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add ladders
    config.addLadder(3, 11); // Ladder from tile 3 to tile 11
    config.addLadder(7, 17); // Ladder from tile 7 to tile 17

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertTrue(board.getTile(3).hasLadder(), "Tile 3 should have a ladder");
    assertEquals(11, board.getTile(3).getLadder().getNumber(), "Ladder should lead to tile 11");

    assertTrue(board.getTile(7).hasLadder(), "Tile 7 should have a ladder");
    assertEquals(17, board.getTile(7).getLadder().getNumber(), "Ladder should lead to tile 17");
  }

  @Test
  public void testCreateBoardFromConfig_WithWormholes() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add wormholes
    config.addWormhole(10);
    config.addWormhole(20);

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertTrue(board.getTile(10).hasWormhole(), "Tile 10 should have a wormhole");
    assertNotNull(board.getTile(10).getWormhole(), "Wormhole should have a destination");

    assertTrue(board.getTile(20).hasWormhole(), "Tile 20 should have a wormhole");
    assertNotNull(board.getTile(20).getWormhole(), "Wormhole should have a destination");

    // Verify wormhole isn't to the same tile
    assertNotEquals(10, board.getTile(10).getWormhole().getNumber(),
        "Wormhole destination should not be the same as source");
    assertNotEquals(20, board.getTile(20).getWormhole().getNumber(),
        "Wormhole destination should not be the same as source");
  }

  @Test
  public void testCreateBoardFromConfig_ComplexBoard() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(6);
    config.setColumns(6);
    config.setName("Complex Board");
    config.setDescription("Board with multiple features");

    // Add mixed features
    config.addSnake(35, 7);
    config.addLadder(4, 26);
    config.addWormhole(15);

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertEquals(36, board.getTiles().size(), "Board should have 36 tiles");

    // Check snake
    assertTrue(board.getTile(35).hasSnake(), "Tile 35 should have a snake");
    assertEquals(7, board.getTile(35).getSnake().getNumber(), "Snake should lead to tile 7");

    // Check ladder
    assertTrue(board.getTile(4).hasLadder(), "Tile 4 should have a ladder");
    assertEquals(26, board.getTile(4).getLadder().getNumber(), "Ladder should lead to tile 26");

    // Check wormhole
    assertTrue(board.getTile(15).hasWormhole(), "Tile 15 should have a wormhole");
  }

  @Test
  public void testCreateBoardFromConfig_EmptyLists() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(4);
    config.setColumns(4);

    // Act
    LadderBoard board = LadderGameFactory.createBoardFromConfig(config);

    // Assert
    assertEquals(16, board.getTiles().size(), "Board should have 16 tiles");

    // Check no tiles have special actions
    for (int i = 1; i <= 16; i++) {
      assertFalse(board.getTile(i).hasAction(),
          "Tile " + i + " should not have any actions");
    }
  }

  // NEGATIVE TESTS

  @Test
  public void testCreateBoardFromConfig_InvalidSnake() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add invalid snake (outside board bounds)
    config.addSnake(30, 5); // 30 is outside the 5x5 board

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LadderGameFactory.createBoardFromConfig(config));

    assertTrue(exception.getMessage().contains("Invalid tile number"),
        "Exception message should mention invalid tile number");
  }

  @Test
  public void testCreateBoardFromConfig_InvalidLadder() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add invalid ladder (outside board bounds)
    config.addLadder(5, 30); // 30 is outside the 5x5 board

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LadderGameFactory.createBoardFromConfig(config));

    assertTrue(exception.getMessage().contains("Invalid tile number"),
        "Exception message should mention invalid tile number");
  }

  @Test
  public void testCreateBoardFromConfig_InvalidWormhole() {
    // Arrange
    BoardConfig config = new BoardConfig();
    config.setRows(5);
    config.setColumns(5);

    // Add invalid wormhole (outside board bounds)
    config.addWormhole(30); // 30 is outside the 5x5 board

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LadderGameFactory.createBoardFromConfig(config));

    assertTrue(exception.getMessage().contains("Invalid tile number"),
        "Exception message should mention invalid tile number");
  }

  @Test
  public void testCreateBoardFromConfig_NullConfig() {
    // Act & Assert
    Exception exception = assertThrows(NullPointerException.class,
        () -> LadderGameFactory.createBoardFromConfig(null));

    assertTrue(exception.getMessage().contains("config"),
        "Exception message should mention null config");
  }
}