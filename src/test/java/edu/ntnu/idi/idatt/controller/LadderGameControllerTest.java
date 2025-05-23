package edu.ntnu.idi.idatt.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.ntnu.idi.idatt.model.LadderBoard;
import edu.ntnu.idi.idatt.model.Player;

class LadderGameControllerTest {

  private LadderGameController controller;
  private List<Player> players;
  private Player player1;
  private Player player2;

  @BeforeEach
  public void setUp() {
    // Create a new controller for each test
    controller = new LadderGameController();

    // Set up a test player list
    player1 = new Player("Player1", "#FF0000", 1);
    player2 = new Player("Player2", "#0000FF", 1);
    players = new ArrayList<>();
    players.add(player1);
    players.add(player2);

    // Initialize the game with players
    controller.setupGame(players);
  }

  // POSITIVE TESTS

  @Test
  void constructorShouldCreateValidBoard() {
    // A new controller should create a default 10x9 board
    assertNotNull(controller.getGameBoard(), "Game board should be initialized");
    assertEquals(10, controller.getLadderBoard().getRows(), "Default board should have 10 rows");
    assertEquals(9, controller.getLadderBoard().getColumns(), "Default board should have 9 columns");
  }

  @Test
  void getLadderBoardShouldReturnCorrectBoardType() {
    // Verify the board is a LadderBoard
    assertTrue(controller.getGameBoard() instanceof LadderBoard, "Game board should be a LadderBoard");
    assertEquals(LadderBoard.class, controller.getLadderBoard().getClass(),
        "getLadderBoard should return a LadderBoard");
  }

  @Test
  void rollDiceShouldReturnValueInValidRange() {
    // Test dice rolls are between 1-6
    boolean validRange = true;
    for (int i = 0; i < 100; i++) { // Roll 100 times to check
      int roll = controller.rollDice();
      if (roll < 2 || roll > 12) {
        validRange = false;
        break;
      }
    }
    assertTrue(validRange, "Dice rolls should be between 2 and 12");
  }

  @Test
  void checkVictoryShouldReturnTrueWhenPlayerAtLastTile() {
    // Place a player at the last tile
    int lastTile = controller.getLadderBoard().getRows() * controller.getLadderBoard().getColumns();
    player1.setTileId(lastTile);

    // Check victory condition
    assertTrue(controller.checkVictory(player1), "Player at last tile should win");
  }

  @Test
  void checkVictoryShouldReturnTrueWhenPlayerBeyondLastTile() {
    // Place a player beyond the last tile
    int lastTile = controller.getLadderBoard().getRows() * controller.getLadderBoard().getColumns();
    player1.setTileId(lastTile + 5);

    // Check victory condition
    assertTrue(controller.checkVictory(player1), "Player beyond last tile should win");
  }

  @Test
  void switchToNextPlayerShouldRotateThroughPlayerList() {
    // Verify starting state
    assertSame(player1, controller.getCurrentPlayer(), "First player should be current initially");

    // Switch once
    controller.switchToNextPlayer();
    assertSame(player2, controller.getCurrentPlayer(), "Second player should be current after one switch");

    // Switch again - should wrap around
    controller.switchToNextPlayer();
    assertSame(player1, controller.getCurrentPlayer(), "First player should be current after wrapping around");
  }

  @Test
  void setupGameShouldInitializePlayersAtStartingPosition() {
    // Create new players
    List<Player> newPlayers = new ArrayList<>();
    Player newPlayer1 = new Player("NewPlayer1", "#00FF00", 1);
    Player newPlayer2 = new Player("NewPlayer2", "#FFFF00", 1);
    newPlayers.add(newPlayer1);
    newPlayers.add(newPlayer2);

    // Set up game with new players
    controller.setupGame(newPlayers);

    // Verify initialization
    assertEquals(1, newPlayer1.getTileId(), "Player 1 should start at tile 1");
    assertEquals(1, newPlayer2.getTileId(), "Player 2 should start at tile 1");
    assertSame(newPlayer1, controller.getCurrentPlayer(), "First player should be current");
  }

  @Test
  void loadBoardShouldReplaceGameBoard() {
    // Create a new board
    LadderBoard newBoard = new LadderBoard(5, 5);

    // Load the new board
    controller.loadBoard(newBoard);

    // Verify the new board is used
    assertSame(newBoard, controller.getGameBoard(), "New board should be loaded");
    assertEquals(5, controller.getLadderBoard().getRows(), "Board should have 5 rows");
    assertEquals(5, controller.getLadderBoard().getColumns(), "Board should have 5 columns");
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 10, 25, 50 })
  void getActionDestinationShouldReturnSameValueForRegularTiles(int tileNumber) {
    // For a new board with no special tiles set up, all tiles should return
    // themselves
    assertEquals(tileNumber, controller.getActionDestination(tileNumber),
        "Regular tile should return itself as destination");
  }

  @Test
  void hasTileActionShouldReturnFalseForRegularTiles() {
    // New board has no special tiles
    for (int i = 1; i <= 10; i++) {
      assertFalse(controller.hasTileAction(i), "Regular tile should not have actions");
    }
  }

  // NEGATIVE TESTS

  @Test
  void checkVictoryShouldReturnFalseWhenPlayerNotAtLastTile() {
    // Place player not at last tile
    int lastTile = controller.getLadderBoard().getRows() * controller.getLadderBoard().getColumns();
    player1.setTileId(lastTile - 1);

    // Check victory condition
    assertFalse(controller.checkVictory(player1), "Player not at last tile should not win");
  }

  @Test
  void switchToNextPlayerShouldHandleEmptyPlayerList() {
    // Setup with empty list
    controller.setupGame(new ArrayList<>());

    // Switching should not throw exception
    assertDoesNotThrow(() -> controller.switchToNextPlayer(),
        "Switching player with empty list should not throw exception");
  }

  @Test
  void hasTileActionShouldHandleInvalidTileNumber() {
    // Try to check an invalid tile
    try {
      controller.hasTileAction(0); // Invalid tile ID
      fail("Should throw exception for invalid tile ID");
    } catch (IllegalArgumentException e) {
      // Expected exception
      assertTrue(e.getMessage().contains("Invalid tile number"),
          "Exception message should mention invalid tile number");
    }
  }

  @Test
  void getActionDestinationShouldHandleInvalidTileNumber() {
    // Try to get destination for an invalid tile
    try {
      controller.getActionDestination(0); // Invalid tile ID
      fail("Should throw exception for invalid tile ID");
    } catch (IllegalArgumentException e) {
      // Expected exception
      assertTrue(e.getMessage().contains("Invalid tile number"),
          "Exception message should mention invalid tile number");
    }
  }
}
