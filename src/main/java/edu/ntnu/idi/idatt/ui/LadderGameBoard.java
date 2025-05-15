package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.factory.LadderGameFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.ui.components.GameAlert;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LadderGameBoard class represents the main application for the Snakes and
 * Ladders game. It supports 1-5 players with customizable game pieces.
 */
public class LadderGameBoard {

  private static final int TILE_SIZE = 60;

  private Board gameBoard;
  private List<Player> players = new ArrayList<>();
  private final Map<Integer, StackPane> tilesMap = new HashMap<>();
  private Player currentPlayer;
  private int currentPlayerIndex = 0;
  private Label statusLabel;
  private Label gameInfoLabel;
  private InfoTable infoTable;
  private BorderPane root;
  private Dice dice = new Dice();
  private GamePiece gamePiece;

  /**
   * Creates a new LadderGameBoard instance
   */
  public LadderGameBoard() {
    // Initialize empty board - will be replaced when game is created
    this.gameBoard = new Board(10, 9);
  }

  /**
   * Creates a game scene with the specified board type and players
   * 
   * @param boardType    the type of board to load (e.g., "standard", "empty",
   *                     "custom")
   * @param primaryStage the primary stage
   * @param players      the list of players for the game (1-5 players)
   * @return the created game scene
   */
  public Scene createGameScene(String boardType, Stage primaryStage, List<Player> players) {
    root = new BorderPane();
    root.setStyle("-fx-background-color: #F0EFEB;");

    // Load board from JSON
    loadBoardFromJSON(boardType);

    // Initialize players
    if (players != null && !players.isEmpty()) {
      this.players = players;
    } else {
      // If no players provided, create a default single player
      this.players.add(new Player("TopHat", "TopHat", 1));
    }

    // Validate player count
    if (this.players.size() > 5) {
      showAlert("Maximum 5 players supported. Only the first 5 players will be used.");
      this.players = this.players.subList(0, 5);
    }

    // Set the first player as current
    if (!this.players.isEmpty()) {
      currentPlayer = this.players.get(0);
    }

    // Initialize GamePiece with players
    gamePiece = new GamePiece(TILE_SIZE, this.players);

    // Create and set up the game board UI
    GridPane boardGrid = createGameBoardUI();
    StackPane gameBoardPane = new StackPane();

    gameBoardPane.getChildren().add(boardGrid);
    drawSnakesAndLadders(gameBoardPane);
    root.setCenter(gameBoardPane);

    // Add padding around the board
    BorderPane.setMargin(boardGrid, new Insets(20));

    // Create InfoTable instance
    infoTable = new InfoTable();
    VBox controlPanel = infoTable.createControlPanel(this::rollAndMove);
    root.setRight(controlPanel);

    // Get references to UI components from InfoTable
    this.statusLabel = infoTable.getStatusLabel();
    this.gameInfoLabel = infoTable.getGameInfoLabel();

    // Set initial text for labels
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
   * Legacy method for compatibility - creates a game with default players
   * 
   * @param boardType    The type of board to create
   * @param primaryStage The primary stage
   * @return The created scene
   */
  public Scene createGameScene(String boardType, Stage primaryStage) {
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
    gameAlert.showGameAlert(title, message, new LadderGameActionsImpl(this));
  }

  /**
   * Loads a board configuration from JSON using LadderGameFactory
   *
   * @param boardName the name of the board to load
   */
  private void loadBoardFromJSON(String boardName) {
    Optional<Board> loadedBoard = LadderGameFactory.tryCreateBoard(boardName);
    if (loadedBoard.isPresent()) {
      gameBoard = loadedBoard.get();
    } else {
      // Fallback to default board if loading fails
      gameBoard = new Board(10, 9);
      gameBoard.setName("Default Board");
      gameBoard.setDescription("Default board created when loading failed");
    }
  }

  /**
   * Creates a game board UI based on the loaded board configuration
   */
  private GridPane createGameBoardUI() {
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(2);
    gridPane.setVgap(2);

    int rows = gameBoard.getRows();
    int cols = gameBoard.getColumns();

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
  private void drawSnakesAndLadders(StackPane gridPane) {
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

    // Create line with these coordinates

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
   * Handles dice rolling and player movement
   */
  private void rollAndMove() {
    // Disable roll button during animation
    infoTable.setRollEnabled(false);

    // Roll the dice
    int diceValue = dice.rollDice();

    // Display dice value
    infoTable.updateDiceDisplay(diceValue);

    // Move player
    int oldPosition = currentPlayer.getTileId();
    int newPosition = Math.min(oldPosition + diceValue, gameBoard.getRows() * gameBoard.getColumns()); // Limit to board
                                                                                                       // size

    // Store the position before checking for special tiles
    int landedPosition = newPosition;

    // Check for special tiles
    if (gameBoard.getTile(newPosition).hasLadder()) {
      newPosition = gameBoard.getTile(newPosition).getLadder().getNumber();
      gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
          " and climbed a ladder from " + landedPosition + " to " + newPosition);
    } else if (gameBoard.getTile(newPosition).hasSnake()) {
      newPosition = gameBoard.getTile(newPosition).getSnake().getNumber();
      gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
          " and slid down a snake from " + landedPosition + " to " + newPosition);
    } else if (gameBoard.getTile(newPosition).hasWormhole()) {
      // For wormholes, we generate a random movement between -15 and +20 tiles
      int randomMovement = new Random().nextInt(36) - 15; // Range from -15 to +20

      int wormholeResult = Math.max(1, newPosition + randomMovement);
      newPosition = wormholeResult;

      // Create a descriptive message based on the random movement
      if (randomMovement > 0) {
        gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you forward " + randomMovement +
            " spaces to " + newPosition + "!");
      } else if (randomMovement < 0) {
        gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole sent you backward " + Math.abs(randomMovement) +
            " spaces to " + newPosition + "!");
      } else {
        gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
            " and entered a wormhole at " + landedPosition +
            "! The wormhole spun you around but left you in the same place!");
      }
    } else {
      // No special tile
      gameInfoLabel.setText(currentPlayer.getName() + " rolled " + diceValue +
          " and moved from " + oldPosition + " to " + newPosition);
    }

    currentPlayer.setTileId(newPosition);

    // Animate the player movement
    animatePlayerMove(currentPlayer, oldPosition, newPosition);
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
   * Removes all player pieces from the given tile.
   * The player pieces are removed from the tile to prepare for the next move.
   *
   * @param tile the tile from which player pieces are to be removed
   */
  private void removePlayerFromTile(StackPane tile) {
    List<Node> toKeep = new ArrayList<>();

    // Keep only label (tile number)
    for (Node node : tile.getChildren()) {
      if (node instanceof Label) {
        toKeep.add(node);
      }
    }

    // Clear tile completely
    tile.getChildren().clear();
    tile.getChildren().addAll(toKeep);
  }

  /**
   * Animates the player movement from one tile to another.
   * The player piece is moved from the old position to the new position with an
   * animation.
   *
   * @param player       the player whose piece is to be moved
   * @param fromPosition the old position of the player piece
   * @param toPosition   the new position of the player piece
   */
  private void animatePlayerMove(Player player, int fromPosition, int toPosition) {
    int playerIndex = players.indexOf(player);

    // Get tiles for animation
    StackPane fromTile = tilesMap.get(fromPosition);
    StackPane toTile = tilesMap.get(toPosition);

    // Remove player from old position
    removePlayerFromTile(fromTile);

    // Check if other players were on the same tile and add them back
    for (Player otherPlayer : players) {
      if (otherPlayer != player && otherPlayer.getTileId() == fromPosition) {
        gamePiece.addPlayerToTile(otherPlayer, fromPosition, fromTile);
      }
    }

    // Create animating piece using GamePiece
    ImageView playerPiece = gamePiece.createAnimationPiece(playerIndex);
    if (playerPiece == null) {
      // Skip animation if piece creation fails, but still handle the move
      System.err.println("Animation piece creation failed for " + player.getName());
      gamePiece.addPlayerToTile(player, toPosition, toTile);

      // Still check for victory
      if (checkVictory(player, toPosition)) {
        gameInfoLabel.setText(player.getName() + " has won the game!");
        infoTable.setRollEnabled(false);

        // Show victory alert with game options
        showGameOverAlert("Game Over", player.getName() + " has won the game!");
        return;
      }

      switchToNextPlayer();
      infoTable.setRollEnabled(true);
      return;
    }

    // Create animation container
    StackPane animationPane = new StackPane(playerPiece);
    animationPane.setMaxSize(TILE_SIZE * 0.5, TILE_SIZE * 0.5);

    // Get coordinates
    Bounds fromBounds = fromTile.localToScene(fromTile.getBoundsInLocal());
    Bounds toBounds = toTile.localToScene(toTile.getBoundsInLocal());

    // Add to scene
    root.getChildren().add(animationPane);

    // Set start position
    double startX = fromBounds.getMinX() + (fromBounds.getWidth() - playerPiece.getFitWidth()) / 2;
    double startY = fromBounds.getMinY() + (fromBounds.getHeight() - playerPiece.getFitHeight()) / 2;
    animationPane.setTranslateX(startX);
    animationPane.setTranslateY(startY);

    // Set end position
    double endX = toBounds.getMinX() + (toBounds.getWidth() - playerPiece.getFitWidth()) / 2;
    double endY = toBounds.getMinY() + (toBounds.getHeight() - playerPiece.getFitHeight()) / 2;

    // Create animation
    TranslateTransition transition = new TranslateTransition(Duration.millis(500), animationPane);
    transition.setToX(endX);
    transition.setToY(endY);

    // When animation completes
    transition.setOnFinished(e -> {
      root.getChildren().remove(animationPane);

      // Add player to new position using GamePiece
      gamePiece.addPlayerToTile(player, toPosition, toTile); // Check for victory
      if (checkVictory(player, toPosition)) {
        String victoryMessage = player.getName() + " has won the game!";
        gameInfoLabel.setText(victoryMessage);
        infoTable.setRollEnabled(false);

        // Show victory alert with game options
        showGameOverAlert("Game Over", victoryMessage);
        return;
      }

      // Switch to next player
      switchToNextPlayer();

      // Re-enable roll button
      infoTable.setRollEnabled(true);
    });

    transition.play();
  }

  /**
   * Checks if a player has won the game
   * 
   * @param player   The player to check
   * @param position The player's position
   * @return True if the player has won
   */
  private boolean checkVictory(Player player, int position) {
    // Consider the last tile on the board as the winning position
    int winningPosition = gameBoard.getRows() * gameBoard.getColumns();
    return position >= winningPosition;
  }

  /**
   * Switches to the next player
   */
  private void switchToNextPlayer() {
    // Update player index and current player
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentPlayer = players.get(currentPlayerIndex);

    // Update status label
    statusLabel.setText(currentPlayer.getName() + "'s turn");
  }

  /**
   * Resets the current game to its initial state.
   * Players remain the same but positions are reset.
   */
  public void resetGame() {
    // Reset player positions to start position
    for (Player player : players) {
      int oldPosition = player.getTileId();
      player.setTileId(1);

      // Remove player from old position visual
      StackPane oldTile = tilesMap.get(oldPosition);
      if (oldTile != null) {
        oldTile.getChildren().removeIf(node -> node instanceof ImageView &&
            ((ImageView) node).getUserData() != null &&
            ((ImageView) node).getUserData().equals(player.getName()));
      }

      // Add player to start position visual
      StackPane startTile = tilesMap.get(0);
      if (startTile != null) {
        gamePiece.addPlayerToTile(player, 0, startTile);
      }
    }

    // Reset turn to first player
    currentPlayerIndex = 0;
    currentPlayer = players.get(0);

    // Update the UI
    gameInfoLabel.setText(currentPlayer.getName() + "'s turn");
    statusLabel.setText(currentPlayer.getName() + "'s turn");
    infoTable.setRollEnabled(true);
  }

  /**
   * Sets up a new game with player selection.
   */
  public void setupNewGame() {
    // Clear the board and start fresh
    Stage stage = (Stage) root.getScene().getWindow();
  }

}