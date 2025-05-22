package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.controller.TreasureGameController;
import edu.ntnu.idi.idatt.model.GameActions;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureBoardConfig;
import edu.ntnu.idi.idatt.model.TreasureGameActions;
import edu.ntnu.idi.idatt.model.TreasureGameTile;
import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;
import edu.ntnu.idi.idatt.ui.components.AnimationManager;
import edu.ntnu.idi.idatt.ui.components.GameAlert;
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

public class TreasureGameBoardUI implements GameObserver {
    
    private BorderPane root;
    private TreasureGameController controller;
    private InfoTable infoTable;
    private GamePiece gamePiece;
    private AnimationManager animationManager;
    private GameActions gameActions;
    private final int TILE_SIZE = 60;
    private Map<Integer, StackPane> tilesMap = new HashMap<>();
    private TreasureBoardConfig config = new TreasureBoardConfig();
    
    public Scene createGameScene(String boardType, Stage primaryStage, List<Player> players) {
    // Create controller with a 10x10 board
    controller = new TreasureGameController();
    controller.registerObserver(this); // Register this UI as observer for all events

    // Create custom board based on the hardcoded layout
    TreasureBoard board = createCustomBoard();
    controller.loadBoard(board);
    
    // Setup the game with players - this initializes currentPlayer and positions
    controller.setupGame(players);

    // Create game actions for handling restart, new game, exit
    gameActions = new TreasureGameActions(this);

    // Create root layout
    root = new BorderPane();
    root.setPadding(new Insets(20));

    // Create board layout FIRST
    GridPane boardGrid = createBoardGrid();
    root.setCenter(boardGrid);
    
    // Initialize GamePiece AFTER tiles are created
    gamePiece = new GamePiece(TILE_SIZE, players);

    // Initialize AnimationManager
    animationManager = new AnimationManager(root, gamePiece, tilesMap, infoTable, TILE_SIZE);

    // NOW set up player pieces on the board
    setupPlayerPiecesOnBoard(players);
    
    // Create info panel with move counter
    infoTable = new InfoTable();
    root.setRight(infoTable.createControlPanel(() -> {
        // Roll dice and start step movement
        if (!controller.isMoving()) {
            int diceValue = controller.rollDiceAndMove();
            infoTable.updateDiceDisplay(diceValue);
            infoTable.setRollEnabled(false); // Disable roll during movement
            
            // Display instructions for manual movement
            infoTable.getGameInfoLabel().setText("Use arrow keys to move " + 
                controller.getCurrentPlayer().getName() + 
                " (" + diceValue + " moves)");
            
            // Set callback for when a step is complete
            controller.setOnStepComplete(() -> {
                // If movement is complete, re-enable roll button
                if (!controller.isMoving()) {
                    infoTable.setRollEnabled(true);
                }
            });
        }
    }));
    
    // Initialize player turn display
    if (players.size() > 0) {
        infoTable.getStatusLabel().setText(players.get(0).getName() + "'s turn");
    }

    // Create scene
    Scene scene = new Scene(root, 900, 700);

    // Setup keyboard handlers for manual movement
    setupKeyboardHandlers(scene);

    return scene;
}

/**
 * Sets up player pieces on the board at their current positions
 * 
 * @param players List of players to place on the board
 */
private void setupPlayerPiecesOnBoard(List<Player> players) {
    // Group players by their current position
    Map<Integer, List<Player>> playersByPosition = new HashMap<>();
    
    for (Player player : players) {
        int position = player.getTileId();
        playersByPosition.computeIfAbsent(position, k -> new ArrayList<>()).add(player);
    }
    
    // Add players to their respective tiles
    for (Map.Entry<Integer, List<Player>> entry : playersByPosition.entrySet()) {
        int position = entry.getKey();
        List<Player> playersAtPosition = entry.getValue();
        
        StackPane tile = tilesMap.get(position);
        if (tile != null) {
            // Add each player to this tile
            for (Player player : playersAtPosition) {
                gamePiece.addPlayerToTile(player, position, tile);
            }
        }
    }
}    

private TreasureBoard createCustomBoard() {
        TreasureBoard board = new TreasureBoard(config.ROWS, config.COLUMNS);

        // Clear existing tiles
        board.getTiles().clear();

        // Create tiles based on the layout from config
        int tileNumber = 1;
        for (int row = 0; row < config.ROWS; row++) {
            for (int col = 0; col < config.COLUMNS; col++) {
                TreasureGameTile tile = new TreasureGameTile(tileNumber);
                int tileType = config.STANDARD_LAYOUT[row][col];
                tile.setTileType(tileType);
                
                // Debug for specific tiles we're interested in
                if (tileNumber == 85 || tileNumber == 95) {
                    System.out.println("Creating tile " + tileNumber + 
                                      " at row=" + row + ", col=" + col + 
                                      " with type=" + tileType);
                }
                
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
        for (int row = 0; row < config.ROWS; row++) {
            for (int col = 0; col < config.COLUMNS; col++) {
                int tileType = config.STANDARD_LAYOUT[row][col];
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
                background.setFill(Color.TRANSPARENT);
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
        // Use the config to find the start position
        int startTileId = config.findStartPosition();
        if (tilesMap.containsKey(startTileId)) {
            return tilesMap.get(startTileId);
        }

        // Fallback to tile 95 if the start tile is not found
        return tilesMap.get(95);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void resetGame() {
    // Clear all tiles first
    for (StackPane tile : tilesMap.values()) {
        clearTile(tile);
    }
    
    // Reset the controller (this will reset player positions)
    controller.resetGame();
    
    // Re-setup player pieces on the board
    setupPlayerPiecesOnBoard(controller.getPlayers());
    
    // Update UI elements
    if (infoTable != null) {
        Player currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer != null) {
            infoTable.getStatusLabel().setText(currentPlayer.getName() + "'s turn");
        }
        infoTable.updateDiceResult(0);
        infoTable.updateMoveCounter(0);
        infoTable.setRollEnabled(true);
    }
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
    
    @Override
    public void update(GameEvent event) {
        String eventType = event.getType();
        Object data = event.getData();
        
        switch (eventType) {
            case "DICE_ROLLED":
                int diceValue = (Integer) data;
                if (infoTable != null) {
                    infoTable.updateDiceDisplay(diceValue);
                }
                break;
                
            case "MOVE_COUNTER_UPDATED":
                int moveCounter = (Integer) data;
                if (infoTable != null) {
                    infoTable.updateMoveCounter(moveCounter);
                }
                break;
                
            case "PLAYER_MOVED":
                @SuppressWarnings("unchecked")
                Map<String, Object> moveData = (Map<String, Object>) data;
                Player player = (Player) moveData.get("player");
                int fromPosition = (Integer) moveData.get("from");
                int toPosition = (Integer) moveData.get("to");
                
                // Animate player movement
                if (animationManager != null) {
                    animationManager.animatePlayerMove(
                        player,
                        controller.getPlayers(),
                        fromPosition,
                        toPosition,
                        controller.checkVictory(player)
                    );
                }
                break;
                
            case "TREASURE_FOUND":
                @SuppressWarnings("unchecked")
                Map<String, Object> treasureData = (Map<String, Object>) data;
                Player finder = (Player) treasureData.get("player");
                
                // Show treasure found message
                new GameAlert().showGameAlert(
                    "Treasure Found!",
                    finder.getName() + " has found the treasure and won the game!",
                    gameActions
                );
                break;
                
            case "TURN_CHANGED":
                Player currentPlayer = (Player) data;
                if (infoTable != null) {
                    infoTable.getStatusLabel().setText(currentPlayer.getName() + "'s turn");
                }
                break;
                
            case "GAME_WON":
                Player winner = (Player) data;
                new GameAlert().showGameAlert(
                    "Game Over",
                    winner.getName() + " has won the game by finding the treasure!",
                    gameActions
                );
                break;
        }
    }

    /**
     * Set up keyboard event handling for manual movement
     * 
     * @param scene The game scene to attach event handlers to
     */
    private void setupKeyboardHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (!controller.isMoving()) {
                return; // Only handle movement if we've rolled the dice
            }
            
            String direction = null;
            switch (event.getCode()) {
                case UP:
                    direction = "UP";
                    break;
                case DOWN:
                    direction = "DOWN";
                    break;
                case LEFT:
                    direction = "LEFT";
                    break;
                case RIGHT:
                    direction = "RIGHT";
                    break;
                default:
                    return; // Ignore other keys
            }
            
            boolean moved = controller.movePlayerInDirection(direction);
            
            if (!moved) {
                int moveCounter = controller.getMoveCounter();
                if (moveCounter > 0) {
                    infoTable.getGameInfoLabel().setText("Can't move that way. Try another direction.");
                }
            }
        });
    }
}