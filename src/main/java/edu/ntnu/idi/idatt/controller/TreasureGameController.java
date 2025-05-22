package edu.ntnu.idi.idatt.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureGameTile;
import edu.ntnu.idi.idatt.observer.GameEvent;

public class TreasureGameController extends GameController {

    // Track if treasure has been found
    private boolean treasureFound = false;

    public TreasureGameController() {
        super();
        loadBoard(new TreasureBoard(10, 10));
    }

    public void loadBoard(TreasureBoard board) {
        this.gameBoard = board;
    }

    @Override
    public TreasureBoard getGameBoard() {
        return (TreasureBoard) super.getGameBoard();
    }

    /**
     * Roll dice and move player
     * @return The dice roll value
     */
    public int rollDiceAndMove() {
        // Roll the dice
        int diceValue = rollDice();

        // Get player's current position
        Player currentPlayer = getCurrentPlayer();
        int oldPosition = currentPlayer.getTileId();

        // Calculate new position
        int newPosition = findNextPosition(oldPosition, diceValue);

        // Move player to new position
        movePlayer(currentPlayer, oldPosition, newPosition);

        // Process tile actions
        processTileActions(currentPlayer, newPosition);

        // Check for victory
        boolean hasWon = checkVictory(currentPlayer);

        if (!hasWon) {
            // Switch to next player
            switchToNextPlayer();
        }

        return diceValue;
    }

    /**
     * Find the next valid position based on the board layout
     */
    private int findNextPosition(int currentPosition, int moves) {
        TreasureBoard board = getGameBoard();

        // Calculate row and column of current position
        int row = (currentPosition - 1) / 10;
        int col = (currentPosition - 1) % 10;

        // Simple movement implementation
        // In a more complete implementation, this would search for valid paths
        int maxMoves = Math.min(moves, 3); // Limit moves to 3 for simplicity

        // Find a walkable tile in the vicinity
        for (int i = 1; i <= maxMoves; i++) {
            // Try to move in different directions
            int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}}; // right, down, left, up

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if in bounds
                if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10) {
                    // Calculate new tile id
                    int newTileId = newRow * 10 + newCol + 1;

                    // Get tile type
                    TreasureGameTile tile = board.getTile(newTileId);
                    if (tile != null && tile.getTileType() > 0) { // Type > 0 means walkable
                        return newTileId;
                    }
                }
            }
        }

        // If no valid move is found, stay in place
        return currentPosition;
    }

    @Override
    public int processTileActions(Player player, int tileId) {
        TreasureBoard board = getGameBoard();
        TreasureGameTile tile = board.getTile(tileId);

        if (tile != null && tile.getTileType() == 2) { // Treasure location
            // Check if this is a treasure location
            notifyObservers(new GameEvent("TREASURE_FOUND",
                    Map.of("player", player, "position", tileId)));
            treasureFound = true;
        }

        return tileId;
    }

    @Override
    public boolean checkVictory(Player player) {
        return treasureFound;
    }

    public boolean isTileWalkable(int tileId) {
        TreasureBoard board = getGameBoard();
        TreasureGameTile tile = board.getTile(tileId);
        return tile != null && tile.getTileType() > 0;
    }

    public boolean isTreasureLocation(int tileId) {
        TreasureBoard board = getGameBoard();
        TreasureGameTile tile = board.getTile(tileId);
        return tile != null && tile.getTileType() == 2;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}