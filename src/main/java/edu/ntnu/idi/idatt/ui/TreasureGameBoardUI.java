package edu.ntnu.idi.idatt.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.controller.TreasureGameController;
import edu.ntnu.idi.idatt.model.GameActions;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureGameActions;
import edu.ntnu.idi.idatt.model.TreasureGameTile;
import edu.ntnu.idi.idatt.ui.components.AnimationManager;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TreasureGameBoardUI {
    private BorderPane root;
    private TreasureGameController controller;
    private InfoTable infoTable;
    private GamePiece gamePiece;
    private AnimationManager animationManager;
    private GameActions gameActions;
    private final int TILE_SIZE = 60;
    private Map<Integer, StackPane> tilesMap = new HashMap<>();

    // Hardcoded 10x10 board layout
    private final int[][] boardLayout = {
            {0, 2, 1, 1, 2, 1, 2, 1, 0, 0},
            {1, 1, 0, 0, 0, 1, 0, 1, 1, 2},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {2, 1, 1, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 1, 0, 0, 1, 2, 1, 1, 1},
            {0, 2, 1, 1, 1, 1, 0, 0, 1, 0},
            {0, 1, 0, 0, 1, 0, 0, 0, 2, 0},
            {0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
            {0, 0, 0, 0, 3, 0, 0, 0, 0, 0}
    };

    public Scene createGameScene(String boardType, Stage primaryStage, List<Player> players) {
        // Create controller with a 10x10 board
        controller = new TreasureGameController();

        // Create custom board based on the hardcoded layout
        TreasureBoard board = createCustomBoard();
        controller.loadBoard(board);
        controller.setPlayers(players);

        // Create game actions for handling restart, new game, exit
        gameActions = new TreasureGameActions(this);

        // Create root layout
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Create board layout
        GridPane boardGrid = createBoardGrid();
        root.setCenter(boardGrid);

//        // Create info panel
//        infoTable = new InfoTable(players, new Die());
//        root.setRight(infoTable.getRoot());

        // Initialize GamePiece for player visualization
        gamePiece = new GamePiece(TILE_SIZE, players);

        // Initialize AnimationManager for player movement
        animationManager = new AnimationManager(root, gamePiece, tilesMap, infoTable, TILE_SIZE);

        // Add player pieces to the start position (tile with type 3)
        StackPane startTile = findStartTile();
        gamePiece.setupPlayerPieces(startTile);

//        // Set up event handlers
//        setupEventHandlers();        // Create scene
        Scene scene = new Scene(root, 900, 700);
        // Remove stylesheet - using inline styles only

        return scene;
    }

    private TreasureBoard createCustomBoard() {
        TreasureBoard board = new TreasureBoard(10, 10);

        // Clear existing tiles
        board.getTiles().clear();

        // Create tiles based on the layout
        int tileNumber = 1;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                TreasureGameTile tile = new TreasureGameTile(tileNumber);
                tile.setTileType(boardLayout[row][col]);
                board.getTiles().add(tile);
                tileNumber++;
            }
        }

        return board;
    }

    private GridPane createBoardGrid() {
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        int tileId = 1;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int tileType = boardLayout[row][col];
                StackPane tile = createTile(tileId, tileType);
                boardGrid.add(tile, col, row);

                // Store the tile in the map for later reference
                tilesMap.put(tileId, tile);

                tileId++;
            }
        }

        return boardGrid;
    }

    private StackPane createTile(int tileId, int tileType) {
        StackPane tile = new StackPane();
        tile.setPrefSize(TILE_SIZE, TILE_SIZE);

        // Create different tile appearances based on type
        Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE);

        switch (tileType) {
            case 0: // Void - cannot move here
                background.setFill(Color.DARKGRAY);
                break;
            case 1: // Path - can move here
                background.setFill(Color.LIGHTGREEN);
                break;
            case 2: // Treasure location
                background.setFill(Color.GOLD);
                break;
            case 3: // Start position
                background.setFill(Color.LIGHTBLUE);
                break;
            default:
                background.setFill(Color.WHITE);
        }

        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);

        // Add tile number label
        Label tileLabel = new Label(String.valueOf(tileId));
        tileLabel.setStyle("-fx-font-weight: bold;");

        tile.getChildren().addAll(background, tileLabel);

        return tile;
    }

    private StackPane findStartTile() {
        // Find tile with type 3 (start position)
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (boardLayout[row][col] == 3) {
                    int tileId = row * 10 + col + 1;
                    return tilesMap.get(tileId);
                }
            }
        }

        // Fallback to first tile if start not found
        return tilesMap.get(1);
    }

//    private void setupEventHandlers() {
//        // Set up roll button event
//        infoTable.setRollButtonAction(e -> {
//            infoTable.setRollEnabled(false); // Disable roll during animation
//
//            // Get current player
//            Player currentPlayer = controller.getCurrentPlayer();
//            int oldPosition = currentPlayer.getTileId();
//
//            // Roll dice and move
//            int diceValue = controller.rollDiceAndMove();
//
//            // Update dice display
//            infoTable.updateDiceResult(diceValue);
//
//            // Get new position
//            int newPosition = currentPlayer.getTileId();
//
//            // Check if move is valid (path exists)
//            if (isMoveValid(oldPosition, newPosition)) {
//                // Animate movement
//                animationManager.animatePlayerMove(
//                        currentPlayer,
//                        controller.getPlayers(),
//                        oldPosition,
//                        newPosition,
//                        controller.checkVictory(currentPlayer)
//                );
//
//                // Check for victory
//                if (controller.checkVictory(currentPlayer)) {
//                    new GameAlert().showGameAlert(
//                            "Game Over",
//                            currentPlayer.getName() + " has won the game by finding the treasure!",
//                            gameActions
//                    );
//                } else {
//                    // Update current player display
//                    infoTable.updateCurrentPlayer(controller.getCurrentPlayerIndex());
//                    infoTable.setRollEnabled(true);
//                }
//            } else {
//                // Invalid move, reset player position
//                currentPlayer.setTileId(oldPosition);
//                infoTable.setRollEnabled(true);
//            }
//        });
//    }

    private boolean isMoveValid(int fromPosition, int toPosition) {
        // Get row and column for both positions
        int fromRow = (fromPosition - 1) / 10;
        int fromCol = (fromPosition - 1) % 10;
        int toRow = (toPosition - 1) / 10;
        int toCol = (toPosition - 1) % 10;

        // Check if destination is valid (not type 0)
        if (boardLayout[toRow][toCol] == 0) {
            return false;
        }

        // Simple path finding logic could be added here
        // For now, we'll just check if destination is walkable
        return boardLayout[toRow][toCol] > 0;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void resetGame() {
        // Reset player positions
        for (Player player : controller.getPlayers()) {
            player.setTileId(1);
        }

//        // Update UI
//        controller.resetGame();
//        infoTable.updateCurrentPlayer(0);
//        infoTable.updateDiceResult(0);

        // Clear all tiles and re-add players to start
        for (StackPane tile : tilesMap.values()) {
            clearTile(tile);
        }

        // Add players back to start tile
        StackPane startTile = findStartTile();
        gamePiece.setupPlayerPieces(startTile);

        // Enable roll button
        infoTable.setRollEnabled(true);
    }

    private void clearTile(StackPane tile) {
        // Keep only the first two children (background and label)
        if (tile.getChildren().size() > 2) {
            tile.getChildren().remove(2, tile.getChildren().size());
        }
    }

    public TreasureGameController getController() {
        return controller;
    }
}