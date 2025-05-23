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

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureBoardConfig;

class TreasureGameControllerTest {
    
    private TreasureGameController controller;
    private List<Player> players;
    private Player player1;
    private Player player2;
    private TreasureBoardConfig config;

    @BeforeEach
    public void setUp() {
        // Create a new controller for each test
        controller = new TreasureGameController();
        config = new TreasureBoardConfig();
        
        // Set up a test player list
        player1 = new Player("Player1", "#FF0000", config.findStartPosition());
        player2 = new Player("Player2", "#0000FF", config.findStartPosition());
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        // Initialize the game with players
        controller.setupGame(players);
    }

    // POSITIVE TESTS

    @Test
    void constructorShouldCreateValidBoard() {
        // A new controller should create a board with correct dimensions
        assertNotNull(controller.getGameBoard(), "Game board should be initialized");
        assertEquals(10, controller.getGameBoard().getRows(), "Default board should have 10 rows");
        assertEquals(10, controller.getGameBoard().getColumns(), "Default board should have 10 columns");
    }

    @Test
    void getGameBoardShouldReturnCorrectBoardType() {
        // Verify the board is a TreasureBoard
        assertTrue(controller.getGameBoard() instanceof TreasureBoard, "Game board should be a TreasureBoard");
        assertEquals(TreasureBoard.class, controller.getGameBoard().getClass(),
                "getGameBoard should return a TreasureBoard");
    }

    @Test
    void rollDiceAndMoveShouldReturnValueInValidRange() {
        // Test dice rolls are between 1-6 (since TreasureGameController uses 1 die)
        boolean validRange = true;
        for (int i = 0; i < 100; i++) { // Roll 100 times to check
            // Reset the controller to allow multiple rolls
            controller = new TreasureGameController();
            controller.setupGame(players);
            
            int roll = controller.rollDiceAndMove();
            if (roll < 1 || roll > 6) {
                validRange = false;
                break;
            }
        }
        assertTrue(validRange, "Dice rolls should be between 1 and 6");
    }

    @Test
    void checkVictoryShouldReturnTrueWhenTreasureFound() {
        // Set treasure found flag to true using reflection
        java.lang.reflect.Field treasureFoundField;
        try {
            treasureFoundField = TreasureGameController.class.getDeclaredField("treasureFound");
            treasureFoundField.setAccessible(true);
            treasureFoundField.set(controller, true);
            
            // Check victory condition
            assertTrue(controller.checkVictory(player1), "Player should win when treasure is found");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to set treasureFound field: " + e.getMessage());
        }
    }

    @Test
    void checkVictoryShouldReturnFalseWhenTreasureNotFound() {
        // By default, treasure is not found yet
        assertFalse(controller.checkVictory(player1), "Player should not win when treasure is not found");
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
    void setupGameShouldInitializePlayersAtStartPosition() {
        // Create new players
        List<Player> newPlayers = new ArrayList<>();
        Player newPlayer1 = new Player("NewPlayer1", "#00FF00", 1);
        Player newPlayer2 = new Player("NewPlayer2", "#FFFF00", 1);
        newPlayers.add(newPlayer1);
        newPlayers.add(newPlayer2);

        // Set up game with new players
        controller.setupGame(newPlayers);

        // Get the start position
        int startPosition = config.findStartPosition();
        
        // Verify initialization
        assertEquals(startPosition, newPlayer1.getTileId(), "Player 1 should start at the start position");
        assertEquals(startPosition, newPlayer2.getTileId(), "Player 2 should start at the start position");
        assertSame(newPlayer1, controller.getCurrentPlayer(), "First player should be current");
    }

    @Test
    void loadBoardShouldReplaceGameBoard() {
        // Create a new board
        TreasureBoard newBoard = new TreasureBoard(5, 5);

        // Load the new board
        controller.loadBoard(newBoard);

        // Verify the new board is used
        assertSame(newBoard, controller.getGameBoard(), "New board should be loaded");
        assertEquals(5, controller.getGameBoard().getRows(), "Board should have 5 rows");
        assertEquals(5, controller.getGameBoard().getColumns(), "Board should have 5 columns");
    }

    @Test
    void isManualMovementModeShouldReturnTrueByDefault() {
        assertTrue(controller.isManualMovementMode(), "Manual movement mode should be true by default");
    }

    @Test
    void setManualMovementModeShouldChangeMode() {
        // Default is true
        assertTrue(controller.isManualMovementMode(), "Manual movement mode should be true by default");
        
        // Change to false
        controller.setManualMovementMode(false);
        assertFalse(controller.isManualMovementMode(), "Manual movement mode should be false after setting");
        
        // Change back to true
        controller.setManualMovementMode(true);
        assertTrue(controller.isManualMovementMode(), "Manual movement mode should be true after setting");
    }

    @Test
    void getMoveCounterShouldReturnZeroBeforeRoll() {
        assertEquals(0, controller.getMoveCounter(), "Move counter should be 0 before rolling");
    }

    @Test
    void getMoveCounterShouldUpdateAfterRoll() {
        // Roll dice and check if move counter is updated
        int roll = controller.rollDiceAndMove();
        assertEquals(roll, controller.getMoveCounter(), "Move counter should match dice roll");
    }

    @Test
    void isMovingShouldReturnFalseBeforeRoll() {
        assertFalse(controller.isMoving(), "Player should not be moving before roll");
    }

    @Test
    void isMovingShouldReturnTrueAfterRoll() {
        controller.rollDiceAndMove();
        assertTrue(controller.isMoving(), "Player should be moving after roll");
    }

    @Test
    void isTileWalkableShouldReturnCorrectValues() {
        // Test some known walkable tiles (paths, treasure locations, start)
        assertTrue(controller.isTileWalkable(95), "Start position should be walkable");
        
        // Test some known non-walkable tiles (void spaces)
        assertFalse(controller.isTileWalkable(91), "Void space should not be walkable");
    }

    @Test
    void getValidPositionInDirectionShouldReturnCorrectPosition() {
        // Place player at start position
        player1.setTileId(95);
        
        // Roll dice to enable movement
        controller.rollDiceAndMove();
        
        // Check valid directions from start
        int upPosition = controller.getValidPositionInDirection("UP");
        assertEquals(85, upPosition, "Moving UP from start should reach tile 85");
    }

    @Test
    void resetGameShouldResetGameState() {
        // Set up a game in progress
        controller.rollDiceAndMove();
        assertTrue(controller.isMoving(), "Game should be in moving state");
        
        // Reset the game
        controller.resetGame();
        
        // Check that game state is reset
        assertFalse(controller.isMoving(), "Game should not be in moving state after reset");
        assertEquals(0, controller.getMoveCounter(), "Move counter should be 0 after reset");
        assertEquals(config.findStartPosition(), player1.getTileId(), "Player 1 should be at start position");
        assertEquals(config.findStartPosition(), player2.getTileId(), "Player 2 should be at start position");
    }

    // NEGATIVE TESTS

    @Test
    void rollDiceAndMoveShouldReturnZeroWhileAlreadyMoving() {
        // First roll - should work
        controller.rollDiceAndMove();
        
        // Second roll while still moving - should return 0
        int result = controller.rollDiceAndMove();
        assertEquals(0, result, "Rolling while already moving should return 0");
    }

    @Test
    void getValidPositionInDirectionShouldReturnNegativeOneWhenNoMovesLeft() {
        // Set move counter to 0
        java.lang.reflect.Field moveCounterField;
        try {
            moveCounterField = TreasureGameController.class.getDeclaredField("moveCounter");
            moveCounterField.setAccessible(true);
            moveCounterField.set(controller, 0);
            
            // Try to get valid position
            int result = controller.getValidPositionInDirection("UP");
            assertEquals(-1, result, "Should return -1 when no moves left");
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            fail("Failed to set moveCounter field: " + e.getMessage());
        }
    }

    @Test
    void getValidPositionInDirectionShouldReturnNegativeOneForInvalidDirection() {
        // Place player at position 95 (start)
        player1.setTileId(95);
        
        // Roll dice to enable movement
        controller.rollDiceAndMove();
        
        // Try to move in a direction where there is no walkable tile
        int result = controller.getValidPositionInDirection("DOWN");
        assertEquals(-1, result, "Should return -1 for invalid direction");
    }

    @Test
    void movePlayerInDirectionShouldReturnFalseWhenNotMoving() {
        boolean result = controller.movePlayerInDirection("UP");
        assertFalse(result, "Should return false when not in moving state");
    }

    @Test
    void movePlayerInDirectionShouldReturnFalseForInvalidDirection() {
        // Start movement
        controller.rollDiceAndMove();
        
        // Try to move in invalid direction
        boolean result = controller.movePlayerInDirection("INVALID");
        assertFalse(result, "Should return false for invalid direction");
    }

    @Test
    void switchToNextPlayerShouldHandleEmptyPlayerList() {
        // Setup with empty list
        controller.setupGame(new ArrayList<>());

        // Switching should not throw exception
        assertDoesNotThrow(() -> controller.switchToNextPlayer(),
                "Switching player with empty list should not throw exception");
    }
}
