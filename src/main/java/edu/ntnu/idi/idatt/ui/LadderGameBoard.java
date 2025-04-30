package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.factory.LadderGameFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
 * The LadderGameBoard class represents the main application for the Snakes and Ladders
 * game.
 */
public class LadderGameBoard {

    private static final int TILE_SIZE = 60;

    private Board gameBoard;
    private final List<Player> players = new ArrayList<>();
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
     * Creates a game scene with the specified board type
     * @param boardType the type of board to load (e.g., "standard", "empty", "custom")
     * @param primaryStage the primary stage
     * @return the created game scene
     */
    public Scene createGameScene(String boardType, Stage primaryStage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F0EFEB;");

        // Load board from JSON
        loadBoardFromJSON(boardType);

        // Initialize players
        players.add(new Player("Player 1", 1));
        players.add(new Player("Player 2", 1));
        currentPlayer = players.getFirst();

        // Initialize GamePiece after players are created
        gamePiece = new GamePiece(TILE_SIZE, players);

        // Create and set up the game board UI
        GridPane boardGrid = createGameBoardUI();
        root.setCenter(boardGrid);

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

        // Add snakes and ladders to the visualization
        drawSnakesAndLadders(gridPane);

        return gridPane;
    }

    /**
     * Draws snakes and ladders on the grid based on the loaded board configuration
     */
    private void drawSnakesAndLadders(GridPane gridPane) {
        // Draw ladders and snakes after all tiles are created
        for (int i = 1; i <= gameBoard.getRows() * gameBoard.getColumns(); i++) {
            Tile tile = gameBoard.getTile(i);

            // Add ladders
            if (tile.hasLadder()) {
                drawConnection(i, tile.getLadder().getNumber(), true, gridPane);
            }

            // Add snakes
            if (tile.hasSnake()) {
                drawConnection(i, tile.getSnake().getNumber(), false, gridPane);
            }
        }
    }

    /**
     * Draws a connection (snake or ladder) between two tiles
     */
    private void drawConnection(int start, int end, boolean isLadder, GridPane gridPane) {
        StackPane startTile = tilesMap.get(start);
        StackPane endTile = tilesMap.get(end);

        if (startTile == null || endTile == null) return;

        // Get bounds relative to the grid
        Bounds startBounds = startTile.getBoundsInParent();
        Bounds endBounds = endTile.getBoundsInParent();

        // Create a line between the centers
        Line line = new Line(
                startBounds.getCenterX(), startBounds.getCenterY(),
                endBounds.getCenterX(), endBounds.getCenterY()
        );

        // Style based on type (ladder or snake)
        if (isLadder) {
            line.setStroke(Color.GREEN);
            line.getStrokeDashArray().addAll(5.0, 5.0);
        } else {
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
        }

        // Add the line to the gridPane at a lower z-index
        gridPane.getChildren().add(0, line);
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
        int newPosition = Math.min(oldPosition + diceValue, gameBoard.getRows() * gameBoard.getColumns()); // Limit to board size
        currentPlayer.setTileId(newPosition);

        // Update the game info
        String moveInfo = currentPlayer.getName() + " rolled " + diceValue +
                " and moves from tile " + oldPosition + " to tile " + newPosition;
        gameInfoLabel.setText(moveInfo);

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
     * Removes the player pieces from the given tile.
     * The player pieces are removed from the tile to prepare for the next move.
     *
     * @param tile the tile from which the player piece is to be removed
     */
    private void removePlayerFromTile(StackPane tile) {
        List<Node> toKeep = new ArrayList<>();

        // Keep only label (tile number)
        for (Node node : tile.getChildren()) {
            if (node instanceof Label) {
                toKeep.add(node);
            }
        }

        // Clear tile and add back only the preserved nodes
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
        Player otherPlayer = players.get(1 - playerIndex);

        // Get tiles for animation
        StackPane fromTile = tilesMap.get(fromPosition);
        StackPane toTile = tilesMap.get(toPosition);

        // Remove player from old position
        removePlayerFromTile(fromTile);

        // If other player was on the same tile, add them back
        if (otherPlayer.getTileId() == fromPosition) {
            gamePiece.addPlayerToTile(otherPlayer, fromPosition, fromTile);
        }

        // Create animating piece using GamePiece
        ImageView playerPiece = gamePiece.createAnimationPiece(playerIndex);
        if (playerPiece == null) {
            // Skip animation if piece creation fails
            gamePiece.addPlayerToTile(player, toPosition, toTile);
            switchToNextPlayer();
            infoTable.setRollEnabled(true);
            return;
        }

        // Create animation container
        StackPane animationPane = new StackPane(playerPiece);

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
            gamePiece.addPlayerToTile(player, toPosition, toTile);

            // Switch to next player
            switchToNextPlayer();

            // Re-enable roll button
            infoTable.setRollEnabled(true);
        });

        transition.play();
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
}