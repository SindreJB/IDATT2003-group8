package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import edu.ntnu.idi.idatt.model.Player;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Board class represents the main application for the Snakes and Ladders
 * game.
 * It extends the JavaFX Application class and sets up the game board UI.
 */
public class LadderGameBoard extends Application {

    private static final int TILE_SIZE = 60;
    private static final int GRID_ROWS = 10;
    private static final int GRID_COLS = 9;

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
     * Starts the application by setting up the primary stage.
     * Initializes the main layout with a BorderPane and sets its background color.
     * Creates and sets up the game board, adds padding around it, and places it at
     * the center of the layout.
     * Loads the stylesheet for the scene and sets the title of the primary stage.
     * Finally, displays the primary stage.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F0EFEB;");

        // Initialize players (for demonstration)
        players.add(new Player("Player 1", "TopHat", 1));
        players.add(new Player("Player 2", "RaceCar", 1));
        currentPlayer = players.getFirst();

        // Initialize GamePiece after players are created
        gamePiece = new GamePiece(TILE_SIZE, players);

        // Create and set up the game board
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);

        // Add padding around the board
        BorderPane.setMargin(gameBoard, new Insets(20));

        // Create InfoTable instance
        infoTable = new InfoTable();
        VBox controlPanel = infoTable.createControlPanel(this::rollAndMove);
        root.setRight(controlPanel);

        // Get references to UI components from InfoTable
        this.statusLabel = infoTable.getStatusLabel();
        this.gameInfoLabel = infoTable.getGameInfoLabel();

        // Set initial text for labels
        statusLabel.setText(currentPlayer.getName() + "'s turn");
        gameInfoLabel.setText("Game started. Roll the dice to begin.");

        // Setup player pieces on the board
        gamePiece.setupPlayerPieces(tilesMap.get(1));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

        primaryStage.setTitle("Snakes and Ladders");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        int newPosition = Math.min(oldPosition + diceValue, GRID_ROWS * GRID_COLS); // Limit to board size
        currentPlayer.setTileId(newPosition);

        // Update the game info
        String moveInfo = currentPlayer.getName() + " rolled " + diceValue +
                " and moves from tile " + oldPosition + " to tile " + newPosition;
        gameInfoLabel.setText(moveInfo);

        // Animate the player movement
        animatePlayerMove(currentPlayer, oldPosition, newPosition);
    }

    /**
     * Creates a game board represented by a GridPane with tiles arranged in a
     * snake-like pattern.
     * The tiles are styled with the "styled-tile" CSS class.
     *
     * @return a GridPane representing the game board with tiles arranged in a
     *         snake-like pattern.
     */
    private GridPane createGameBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);

        // Create tiles in a snake-like pattern
        int[] tileNumber = { 1 };
        IntStream.rangeClosed(0, GRID_ROWS - 1)
                .map(i -> GRID_ROWS - 1 - i)
                .forEach(row -> {
                    if (row % 2 == 0) {
                        IntStream.rangeClosed(0, GRID_COLS - 1)
                                .map(i -> GRID_COLS - 1 - i)
                                .forEach(col -> {
                                    StackPane tile = createTile(tileNumber[0]);
                                    tile.getStyleClass().add("styled-tile");
                                    gridPane.add(tile, col, row);
                                    tilesMap.put(tileNumber[0], tile); // Store tile reference
                                    tileNumber[0]++;
                                });
                    } else {
                        IntStream.range(0, GRID_COLS)
                                .forEach(col -> {
                                    StackPane tile = createTile(tileNumber[0]);
                                    tile.getStyleClass().add("styled-tile");
                                    gridPane.add(tile, col, row);
                                    tilesMap.put(tileNumber[0], tile); // Store tile reference
                                    tileNumber[0]++;
                                });
                    }
                });

        return gridPane;
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

    /**
     * The main entry point for the Java application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}