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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TreasureGameBoardUI implements GameObserver {

  private BorderPane root;
  private TreasureGameController controller;
  private InfoTable infoTable;
  private GamePiece gamePiece;
  private AnimationManager animationManager;
  private GameActions gameActions;
  private final int TILE_SIZE = 60;
  private final Map<Integer, StackPane> tilesMap = new HashMap<>();
  private final TreasureBoardConfig config = new TreasureBoardConfig();

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
    root.setStyle("-fx-background-color: #fbcc74;");

    // Initialize GamePiece AFTER tiles are created
    gamePiece = new GamePiece(TILE_SIZE, players);

    // Initialize AnimationManager
    animationManager = new AnimationManager(root, gamePiece, tilesMap, infoTable, TILE_SIZE, 100);

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
    if (!players.isEmpty()) {
      infoTable.getStatusLabel().setText(players.get(0).getName() + "'s turn");
    }

    // Create scene
    Scene scene = new Scene(root);

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

        board.getTiles().add(tile);
        tileNumber++;
      }
    }

    // Assign the treasure to one random type 2 tile
    board.assignRandomTreasure();

    return board;
  }

  private GridPane createBoardGrid() {
    GridPane boardGrid = new GridPane();
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setHgap(5);
    boardGrid.setVgap(5);

    // Add background image
    String imagePath = "/edu/ntnu/idi/idatt/view/PirateMap.png";
    BackgroundImage backgroundImage = new BackgroundImage(
        new Image(getClass().getResourceAsStream(imagePath)),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
    boardGrid.setBackground(new Background(backgroundImage));

    int tileId = 1;
    for (int row = 0; row < config.ROWS; row++) {
      for (int col = 0; col < config.COLUMNS; col++) {
        int tileType = config.STANDARD_LAYOUT[row][col];
        StackPane tile = createTile(tileType);
        boardGrid.add(tile, col, row);

        // Store the tile in the map for later reference
        tilesMap.put(tileId, tile);

        tileId++;
      }
    }

    return boardGrid;
  }

  private StackPane createTile(int tileType) {
    StackPane tile = new StackPane();
    tile.setPrefSize(TILE_SIZE, TILE_SIZE);

    if (tileType == 0) {
      tile.setVisible(false);
      return tile;
    }

    switch (tileType) {
      case 1 -> {
        tile.setAlignment(Pos.CENTER);

        GridPane smallTile = new GridPane();
        int smallerSize = TILE_SIZE / 2;
        smallTile.setPrefSize(smallerSize, smallerSize);
        smallTile.setMaxSize(smallerSize, smallerSize);
        smallTile.setMinSize(smallerSize, smallerSize);
        smallTile.setStyle("-fx-background-color:rgba(124, 75, 54, 0.75);" +
            "-fx-padding: 2px;" +
            "-fx-background-radius: " + (smallerSize / 4) + "px;" +
            "-fx-border-radius: " + (smallerSize / 4) + "px;" +
            "-fx-alignment: center;");
        tile.getChildren().add(smallTile);
      }
      case 2 ->
        tile.setStyle("-fx-background-color:rgb(212, 162, 25);" +
            "-fx-padding: 10px 16px;" +
            " -fx-background-radius: 6px;" +
            "-fx-border-radius: 6px;" +
            " -fx-alignment: center;");
      case 3 ->
        tile.setStyle("-fx-background-color:rgb(148, 80, 24);" +
            "-fx-border-color: rgb(212,175,25);" +
            "-fx-padding: 10px 16px;" +
            " -fx-background-radius: 6px;" +
            "-fx-border-radius: 6px;" +
            " -fx-alignment: center;");
      default ->
        tile.setStyle("-fx-background-color:rgb(255, 255, 255);" +
            "-fx-padding: 10px 16px;" +
            " -fx-background-radius: 6px;" +
            "-fx-border-radius: 6px;" +
            " -fx-alignment: center;");
    }
    return tile;
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
    // We need to keep the background rectangle
    // and only remove player pieces (contained in StackPanes)
    List<Node> nodesToRemove = new ArrayList<>();

    for (Node node : tile.getChildren()) {
      // Remove only StackPane containers that contain player pieces
      if (node instanceof StackPane && node != tile) {
        nodesToRemove.add(node);
      }
    }

    // Remove just the player containers
    tile.getChildren().removeAll(nodesToRemove);
  }

  public TreasureGameController getController() {
    return controller;
  }

  @Override
  public void update(GameEvent event) {
    String eventType = event.getType();
    Object data = event.getData();

    switch (eventType) {
      case "DICE_ROLLED" -> {
        int diceValue = (Integer) data;
        if (infoTable != null) {
          infoTable.updateDiceDisplay(diceValue);
        }
      }
      case "MOVE_COUNTER_UPDATED" -> {
        int moveCounter = (Integer) data;
        if (infoTable != null) {
          infoTable.updateMoveCounter(moveCounter);
        }
      }
      case "PLAYER_MOVED" -> {
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
              controller.checkVictory(player));
        }
      }
      case "TREASURE_FOUND" -> {
        @SuppressWarnings("unchecked")
        Map<String, Object> treasureData = (Map<String, Object>) data;
        Player finder = (Player) treasureData.get("player");

        // Check if realTreasure exists and safely unbox it
        Object realTreasureObj = treasureData.get("realTreasure");
        boolean realTreasure = realTreasureObj != null ? (Boolean) realTreasureObj : true;

        if (realTreasure) {
          // Show real treasure found message - Star of Africa
          new GameAlert().showGameAlert(
              "Star of Africa Found!",
              finder.getName() + " dug up the Star of Africa and won the game!",
              gameActions);
        } else {
          // Show just dirt message
          if (infoTable != null) {
            infoTable.getGameInfoLabel().setText(
                finder.getName() + " dug up nothing but dirt.");
          }
          // Continue the game after a short delay
          controller.switchToNextPlayer();
        }
      }
      case "TREASURE_TILE" -> {
        @SuppressWarnings("unchecked")
        Map<String, Object> treasureTileData = (Map<String, Object>) data;
        Player nearTreasure = (Player) treasureTileData.get("player");
        int movesLeft = (Integer) treasureTileData.get("movesLeft");

        // Just update the info label to show the player has found a treasure but has
        // moves left
        if (infoTable != null) {
          infoTable.getGameInfoLabel().setText(
              nearTreasure.getName() + " found a treasure but has " +
                  movesLeft + " moves left. Move to another tile or use all moves to win!");
        }
      }
      case "TURN_CHANGED" -> {
        Player currentPlayer = (Player) data;
        if (infoTable != null) {
          infoTable.getStatusLabel().setText(currentPlayer.getName() + "'s turn");
          infoTable.setRollEnabled(true); // Re-enable roll button
        }
      }
      case "GAME_WON" -> {
        Player winner = (Player) data;
        new GameAlert().showGameAlert(
            "Game Over",
            winner.getName() + " has won the game by finding the treasure!",
            gameActions);
      }
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

      String direction;

      direction = switch (event.getCode()) {
        case UP -> "UP";
        case DOWN -> "DOWN";
        case LEFT -> "LEFT";
        case RIGHT -> "RIGHT";
        default -> null; // Ignore other keys
      };

      if (direction != null) {
        boolean moved = controller.movePlayerInDirection(direction);

        if (!moved) {
          int moveCounter = controller.getMoveCounter();
          if (moveCounter > 0) {
            infoTable.getGameInfoLabel().setText("Can't move that way. Try another direction.");
          }
        }
      }
    });
  }
}