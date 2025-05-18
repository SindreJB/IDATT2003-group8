package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.exceptions.InitializeLadderGameException;
import edu.ntnu.idi.idatt.exceptions.LadderGameException;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.observer.GameEvent;
import edu.ntnu.idi.idatt.observer.GameObserver;
import edu.ntnu.idi.idatt.ui.components.AnimationManager;
import edu.ntnu.idi.idatt.ui.components.GameAlert;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;
import edu.ntnu.idi.idatt.ui.components.PlayerSelectionModal;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * The LadderGameBoard class represents the UI for the Snakes and
 * Ladders game. It supports 1-5 players with customizable game pieces.
 * Implements GameObserver to receive updates from the game model.
 */
public class LadderGameBoard implements GameObserver {

  private static final int TILE_SIZE = 60;

  // UI components
  private final Map<Integer, StackPane> tilesMap = new HashMap<>();
  private Label statusLabel;
  private Label gameInfoLabel;
  private InfoTable infoTable;
  private BorderPane root;
  private GamePiece gamePiece;
  private AnimationManager animationManager;

  // Game controller manages all game logic
  private final GameController gameController;

  /**
   * Creates a new LadderGameBoard instance
   */
  public LadderGameBoard() {
    // Initialize the game controller
    this.gameController = new GameController();

    // Set up callbacks from controller to UI
    setupControllerCallbacks();
  }

  /**
   * Sets up callbacks from the GameController to update the UI
   */
  private void setupControllerCallbacks() {
    // Update UI when player turn changes
    gameController.setOnTurnChanged(() -> {
      Player currentPlayer = gameController.getCurrentPlayer();
      if (statusLabel != null) {
        statusLabel.setText(currentPlayer.getName() + "'s turn");
      }
    });

    // Handle dice roll results with UI feedback
    gameController.setOnDiceRolled((diceValue, message, oldPosition, newPosition) -> {
      if (infoTable != null) {
        infoTable.updateDiceDisplay(diceValue);
      }

      if (gameInfoLabel != null) {
        gameInfoLabel.setText(message);
      }
    });

    // Handle detailed player movement for animation
    gameController.setOnPlayerMovement((player, oldPosition, newPosition, checkVictory) -> {
      animatePlayerMove(player, oldPosition, newPosition, checkVictory);
    });

    // Handle game won event
    gameController.setOnGameWon(() -> {
      Player winner = gameController.getCurrentPlayer();
      String victoryMessage = winner.getName() + " has won the game!";
      gameInfoLabel.setText(victoryMessage);

      if (infoTable != null) {
        infoTable.setRollEnabled(false);
      }

      // Show victory alert with game options
      showGameOverAlert("Game Over", victoryMessage);
    });
  }

  /**
   * Creates a game scene with the specified board type and players
   * 
   * @param boardType    the type of board to load (e.g., "standard", "empty",
   *                     "custom")
   * @param primaryStage the primary stage
   * @param players      the list of players for the game (1-5 players)
   * @return the created game scene
   * @throws LadderGameException if there's an issue creating the game board
   */
  public Scene createGameScene(String boardType, Stage primaryStage, List<Player> players) throws LadderGameException {
    root = new BorderPane();
    root.setStyle("-fx-background-color: #F0EFEB;");

    // Register this view as an observer before loading board and players
    gameController.registerObserver(this, "PLAYER_MOVED", "TURN_CHANGED", "GAME_WON", "DICE_ROLLED");

    // Load board and set up players using the controller
    gameController.loadBoard(boardType);
    gameController.setupGame(players);

    // Get board from controller for UI setup
    Board gameBoard = gameController.getGameBoard();
    List<Player> gamePlayers = gameController.getPlayers();

    // Validate player count in UI
    if (gamePlayers.size() > 5) {
      showAlert("Maximum 5 players supported. Only the first 5 players will be used.");
    }

    // Initialize GamePiece with players
    gamePiece = new GamePiece(TILE_SIZE, gamePlayers);

    // Create and set up the game board UI
    GridPane boardGrid = createGameBoardUI(gameBoard);
    StackPane gameBoardPane = new StackPane();

    gameBoardPane.getChildren().add(boardGrid);
    drawSnakesAndLadders(gameBoardPane, gameBoard);
    root.setCenter(gameBoardPane);

    // Add padding around the board
    BorderPane.setMargin(boardGrid, new Insets(20));

    // Create InfoTable instance with roll action delegate to controller
    infoTable = new InfoTable();
    VBox controlPanel = infoTable.createControlPanel(() -> {
      infoTable.setRollEnabled(false); // Disable roll button during turn
      gameController.rollDiceAndMove();
    });
    root.setRight(controlPanel);

    // Get references to UI components from InfoTable
    this.statusLabel = infoTable.getStatusLabel();
    this.gameInfoLabel = infoTable.getGameInfoLabel();

    // Initialize the AnimationManager
    this.animationManager = new AnimationManager(root, gamePiece, tilesMap, infoTable, TILE_SIZE);

    // Set initial text for labels
    Player currentPlayer = gameController.getCurrentPlayer();
    statusLabel.setText(currentPlayer.getName() + "'s turn");
    gameInfoLabel.setText("Game started: " + gameBoard.getName() + "\n" + gameBoard.getDescription());

    // Setup player pieces on the board
    gamePiece.setupPlayerPieces(tilesMap.get(1));

    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

    primaryStage.setTitle("Snakes and Ladders - " + gameBoard.getName());

    return scene;
  }

  /**
   * Implementation of the GameObserver interface's update method.
   * Handles different types of game events.
   * 
   * 
   * @param event The game event to handle
   */
  @Override
  public void update(GameEvent event) {
    if (event == null)
      return;

    // Process events based on their type
    switch (event.getType()) {
      case "DICE_ROLLED":
        Platform.runLater(() -> {
          if (infoTable != null && event.getData() instanceof Integer) {
            infoTable.updateDiceDisplay((Integer) event.getData());
          }
        });
        break;

      case "PLAYER_MOVED":
        Platform.runLater(() -> {
          if (event.getData() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) event.getData();
            Player player = (Player) data.get("player");
            int oldPosition = (Integer) data.get("from");
            int newPosition = (Integer) data.get("to");
            boolean checkVictory = (Boolean) data.getOrDefault("checkVictory", false);

            // Use the existing animation system
            animatePlayerMove(player, oldPosition, newPosition, checkVictory);
          }
        });
        break;

      case "TURN_CHANGED":
        Platform.runLater(() -> {
          if (statusLabel != null && event.getData() instanceof Player) {
            Player player = (Player) event.getData();
            statusLabel.setText(player.getName() + "'s turn");
          }
        });
        break;

      case "GAME_WON":
        Platform.runLater(() -> {
          if (event.getData() instanceof Player) {
            Player winner = (Player) event.getData();
            String victoryMessage = winner.getName() + " has won the game!";

            if (gameInfoLabel != null) {
              gameInfoLabel.setText(victoryMessage);
            }

            if (infoTable != null) {
              infoTable.setRollEnabled(false);
            }

            // Show victory alert
            showGameOverAlert("Game Over", victoryMessage);
          }
        });
        break;

      case "LADDER_CLIMBED":
      case "SNAKE_SLIDE":
      case "WORMHOLE_TELEPORT":
        if (event.getData() instanceof Map) {
          @SuppressWarnings("unchecked")
          Map<String, Object> data = (Map<String, Object>) event.getData();
          handleSpecialTileEvent(event.getType(), data);
        }
        break;

      case "BOARD_LOADED":
        Platform.runLater(() -> {
          if (event.getData() instanceof Board) {
            Board board = (Board) event.getData();
            if (gameInfoLabel != null) {
              gameInfoLabel.setText("Board loaded: " + board.getName() + "\n" + board.getDescription());
            }
          }
        });
        break;

      case "ERROR":
        Platform.runLater(() -> {
          if (event.getData() != null) {
            showAlert(event.getData().toString());
          }
        });
        break;
    }
  }

  /**
   * Handle special tile events like ladder, snake, or wormhole
   * 
   * @param eventType The type of event
   * @param data      Event data map
   */
  private void handleSpecialTileEvent(String eventType, Map<String, Object> data) {
    if (gameInfoLabel == null)
      return;

    Platform.runLater(() -> {
      Player player = (Player) data.get("player");
      int from = (Integer) data.get("from");
      int to = (Integer) data.get("to");

      String message = "";
      switch (eventType) {
        case "LADDER_CLIMBED":
          message = player.getName() + " climbed a ladder from " + from + " to " + to;
          break;
        case "SNAKE_SLIDE":
          message = player.getName() + " slid down a snake from " + from + " to " + to;
          break;
        case "WORMHOLE_TELEPORT":
          int movement = (Integer) data.get("movement");
          if (movement > 0) {
            message = player.getName() + " was teleported forward " + movement + " spaces by a wormhole!";
          } else if (movement < 0) {
            message = player.getName() + " was teleported backward " + Math.abs(movement) + " spaces by a wormhole!";
          } else {
            message = player.getName() + " entered a wormhole but came out in the same place!";
          }
          break;
      }

      gameInfoLabel.setText(message);
    });
  }

  /**
   * Creates a game scene with the specified board type and players
   * 
   * @param boardType    the type of board to load (e.g., "standard", "empty",
   *                     "custom")
   * @param primaryStage the primary stage
   * @return the created game scene
   * 
   */
  public Scene createGameScene(String boardType, Stage primaryStage) throws LadderGameException {
    // Create a single default player
    List<Player> defaultPlayers = new ArrayList<>();
    defaultPlayers.add(new Player("Player 1", "TopHat", 1));

    return createGameScene(boardType, primaryStage, defaultPlayers);
  }

  /**
   * Shows an alert message
   * 
   * @param message The message to show
   */
  private void showAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Game Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Shows a game-over alert with options to restart, start a new game, or exit
   * 
   * @param title   The alert title
   * @param message The message to show
   */
  private void showGameOverAlert(String title, String message) {
    GameAlert gameAlert = new GameAlert();
    gameAlert.showGameAlert(title, message, new LadderGameActions(this));
  }

  /**
   * Creates a game board UI based on the loaded board configuration
   * 
   * @throws LadderGameException If the board has invalid dimensions
   */
  private GridPane createGameBoardUI(Board gameBoard) throws LadderGameException {

    int rows = gameBoard.getRows();
    int cols = gameBoard.getColumns();

    if (rows <= 0 || cols <= 0) {
      throw new LadderGameException("Invalid board dimensions: " + rows + "x" + cols);
    }

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(2);
    gridPane.setVgap(2);

    // Create tiles in a snake-like pattern
    for (int row = 0; row < rows; row++) {
      int actualRow = rows - 1 - row; // Flip row order (bottom to top)

      if (actualRow % 2 == 0) {
        // Even rows go right to left
        for (int col = cols - 1; col >= 0; col--) {
          int tileNumber = row * cols + (cols - col);
          StackPane tile = createTile(tileNumber);
          tile.getStyleClass().add("styled-tile");
          gridPane.add(tile, col, actualRow);
          tilesMap.put(tileNumber, tile);
        }
      } else {
        // Odd rows go left to right
        for (int col = 0; col < cols; col++) {
          int tileNumber = row * cols + col + 1;
          StackPane tile = createTile(tileNumber);
          tile.getStyleClass().add("styled-tile");
          gridPane.add(tile, col, actualRow);
          tilesMap.put(tileNumber, tile);
        }
      }
    }
    return gridPane;
  }

  /**
   * Draws snakes and ladders on the grid based on the loaded board configuration
   */
  private void drawSnakesAndLadders(StackPane gridPane, Board gameBoard) {
    // Draw ladders and snakes after all tiles are created
    javafx.application.Platform.runLater(() -> {
      for (int i = 1; i <= gameBoard.getRows() * gameBoard.getColumns(); i++) {
        Tile tile = gameBoard.getTile(i);
        // Add action graphics to the tile
        if (tile.hasAction()) {
          drawConnection(tile, gridPane);
        }
      }
    });
  }

  /**
   * Draws a connection (snake, ladder, or wormhole) between two tiles
   */
  private void drawConnection(Tile tile, StackPane pane) {
    StackPane startTile = tilesMap.get(tile.getNumber());
    StackPane endTile;

    if (tile.hasLadder()) {
      endTile = tilesMap.get(tile.getLadder().getNumber());
    } else if (tile.hasSnake()) {
      endTile = tilesMap.get(tile.getSnake().getNumber());
    } else if (tile.hasWormhole()) {
      endTile = tilesMap.get(tile.getWormhole().getNumber());
    } else {
      return; // No connection to draw
    }

    Bounds startBounds = startTile.localToScene(startTile.getBoundsInLocal());
    Bounds endBounds = endTile.localToScene(endTile.getBoundsInLocal());

    // Calculate center points
    double startX = startBounds.getMinX() + startBounds.getWidth() / 2;
    double startY = startBounds.getMinY() + startBounds.getHeight() / 2;
    double endX = endBounds.getMinX() + endBounds.getWidth() / 2;
    double endY = endBounds.getMinY() + endBounds.getHeight() / 2;

    // Adjust start and end positions based on connection type
    if (tile.hasLadder()) {
      startY += startBounds.getHeight() * 0.3; // Start from lower part of tile
      endY -= endBounds.getHeight() * 0.3; // End at upper part of tile
    } else if (tile.hasSnake()) {
      startY -= startBounds.getHeight() * 0.3; // Start from upper part of tile
      endX += startBounds.getWidth() * 0.2; // Offset horizontally for visual interest
      endY += endBounds.getHeight() * 0.3; // End at lower part of tile
    } else if (tile.hasWormhole()) {
      // Create a slight curve for wormholes
      startX += startBounds.getWidth() * 0.1;
    }

    // Style based on type
    if (tile.hasLadder()) {
      // Style the ladder starting tile with green background
      Line line = new Line(startX, startY, endX, endY);

      startTile.setStyle("-fx-background-color:rgb(37, 111, 37);"); // Light green background for ladder start
      endTile.setStyle("-fx-background-color:rgb(15, 42, 20);"); // Dark green background for ladder end
      line.setStroke(Color.GREEN);
      line.getStrokeDashArray().addAll(5.0, 5.0);
      root.getChildren().add(line);
      line.toBack();

    } else if (tile.hasSnake()) {
      Line line = new Line(startX, startY, endX, endY);

      startTile.setStyle("-fx-background-color:rgb(111, 37, 37);"); // Light red background for snake head
      endTile.setStyle("-fx-background-color:rgb(42, 15, 15);"); // Dark red background for snake tail
      line.setStroke(Color.RED);
      line.setStrokeWidth(2);
      root.getChildren().add(line);
      line.toBack();

    } else if (tile.hasWormhole()) {
      // Make wormholes purple with a distinct dashed pattern
      startTile.setStyle("-fx-background-color:rgb(75, 0, 130);"); // Purple background for wormhole entrance
    }

    // Add the line to the root pane
    pane.toBack();
  }

  /**
   * Creates a tile represented by a StackPane with a specified number.
   * The tile has a preferred size defined by TILE_SIZE and contains a label
   * displaying the given number.
   *
   * @param number the number to be displayed on the tile
   * @return a StackPane representing the tile with the specified number
   */
  private StackPane createTile(int number) {
    StackPane tile = new StackPane();
    tile.setPrefSize(TILE_SIZE, TILE_SIZE);

    // Create tile number
    Label text = new Label(String.valueOf(number));
    text.getStyleClass().add("styled-text");

    // Add background and number to tile
    tile.getChildren().addAll(text);

    return tile;
  }

  /**
   * Animates the player movement from one tile to another.
   * Delegates to the AnimationManager component.
   *
   * @param player       the player whose piece is to be moved
   * @param fromPosition the old position of the player piece
   * @param toPosition   the new position of the player piece
   * @param checkVictory whether to check for victory after animation
   */
  private void animatePlayerMove(Player player, int fromPosition, int toPosition, boolean checkVictory) {
    List<Player> players = gameController.getPlayers();
    animationManager.animatePlayerMove(player, players, fromPosition, toPosition, checkVictory);
  }

  /**
   * Resets the current game to its initial state.
   * UI delegate for the controller's resetGame method.
   * 
   * @throws InitializeLadderGameException if player pieces cannot be reset
   *                                       properly
   */
  public void resetGame() throws InitializeLadderGameException {
    List<Player> players = gameController.getPlayers();
    // Clear all player pieces from board
    for (Player player : players) {
      int oldPosition = player.getTileId();
      StackPane oldTile = tilesMap.get(oldPosition);
      if (oldTile != null) {
        oldTile.getChildren().removeIf(node -> !(node instanceof Label));
      } else {
        throw new InitializeLadderGameException(
            "Failed to find tile for player '" + player.getName() + "' at position " + oldPosition);
      }
    }

    // Reset game state through controller
    try {
      gameController.resetGame();
    } catch (Exception e) {
      throw new InitializeLadderGameException("Error while resetting game state.", e);
    }

    // Update the UI after reset
    Player currentPlayer = gameController.getCurrentPlayer();
    StackPane startTile = tilesMap.get(1);
    if (startTile == null) {
      throw new InitializeLadderGameException("Start tile not found during reset.");
    }
    for (Player player : players) {
      try {
        gamePiece.addPlayerToTile(player, 1, startTile);
      } catch (Exception e) {
        throw new InitializeLadderGameException("Failed to add player '" + player.getName() + "' to start tile.", e);
      }
    }
    // Update UI labels
    if (currentPlayer != null) {
      gameInfoLabel.setText(currentPlayer.getName() + "'s turn");
      statusLabel.setText(currentPlayer.getName() + "'s turn");
    }
    infoTable.setRollEnabled(true);
  }

  /**
   * Sets up a new game with player selection.
   */
  public void setupNewGame() {
    // Unregister this observer first to avoid duplicate notifications
    gameController.unregisterObserver(this);

    // Get the current stage from the scene
    Stage stage = (Stage) root.getScene().getWindow();

    // Show player selection modal
    PlayerSelectionModal playerSelector = new PlayerSelectionModal(
        gameController.getGameBoard().getName(), stage);
    playerSelector.showModal();
  }

  /**
   * Gets the game controller
   * 
   * @return The game controller
   */
  public GameController getGameController() {
    return gameController;
  }
}