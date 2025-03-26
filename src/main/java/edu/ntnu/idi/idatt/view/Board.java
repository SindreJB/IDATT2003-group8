package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class Board extends Application {

    private static final int TILE_SIZE = 60;
    private static final int GRID_ROWS = 10;
    private static final int GRID_COLS = 9;

    private List<Player> players = new ArrayList<>();
    private Map<Integer, StackPane> tilesMap = new HashMap<>();
    private Player currentPlayer;
    private int currentPlayerIndex = 0;
    private Label statusLabel;
    private HBox diceContainer;
    private Label gameInfoLabel;

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
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F0EFEB;");

        // Initialize players (for demonstration)
        players.add(new Player("Player 1", 1));
        players.add(new Player("Player 2", 1));
        currentPlayer = players.get(0);

        // Create and set up the game board
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);

        // Add padding around the board
        BorderPane.setMargin(gameBoard, new Insets(20));

        // Create control panel
        VBox controlPanel = createControlPanel();
        root.setRight(controlPanel);

        // Setup player pieces on the board
        setupPlayerPieces();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Snakes and Ladders");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setAlignment(Pos.TOP_CENTER);

        statusLabel = new Label(currentPlayer.getName() + "'s turn");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button rollButton = new Button("Roll Dice");
        rollButton.setOnAction(e -> moveOneTile(currentPlayer));

        // Create a space for displaying dice
        diceContainer = new HBox(10);  // Use the class field directly
        diceContainer.setAlignment(Pos.CENTER);
        diceContainer.setPrefHeight(60);
        diceContainer.setPrefWidth(100);
        diceContainer.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f8f8f8;");

        // Create a text area for game info
        gameInfoLabel = new Label("Game information will appear here");  // Use the class field directly
        gameInfoLabel.setWrapText(true);
        gameInfoLabel.setPrefWidth(200);
        gameInfoLabel.setPrefHeight(100);
        gameInfoLabel.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: #f8f8f8;");

        panel.getChildren().addAll(statusLabel, rollButton, diceContainer, gameInfoLabel);
        return panel;
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
     * Sets up player pieces on the game board.
     * The player pieces are represented by images of the players.
     * If the images are not available, colored circles are used as fallback.
     */
    private void setupPlayerPieces() {
        try {
            // Get images for player pieces
            Image player1Image = new Image(getClass().getResourceAsStream("/boardPieces/SindreImage.png"));
            Image player2Image = new Image(getClass().getResourceAsStream("/boardPieces/StianImage.png"));

            // Create ImageViews for player pieces
            ImageView player1Piece = new ImageView(player1Image);
            ImageView player2Piece = new ImageView(player2Image);

            // Scale images appropriately - make them smaller
            player1Piece.setFitHeight(TILE_SIZE * 0.35);
            player1Piece.setFitWidth(TILE_SIZE * 0.35);
            player1Piece.setPreserveRatio(true);

            player2Piece.setFitHeight(TILE_SIZE * 0.35);
            player2Piece.setFitWidth(TILE_SIZE * 0.35);
            player2Piece.setPreserveRatio(true);

            // Both players start at position 1
            StackPane startTile = tilesMap.get(1);

            // Use StackPane instead of HBox to overlap player pieces slightly
            StackPane playerContainer = new StackPane();

            // Add padding around the pieces to offset them slightly
            StackPane.setMargin(player1Piece, new Insets(0, 5, 0, -5));  // Right padding
            StackPane.setMargin(player2Piece, new Insets(0, -5, 0, 5));  // Left padding

            playerContainer.getChildren().addAll(player1Piece, player2Piece);
            playerContainer.setMaxSize(TILE_SIZE, TILE_SIZE);  // Limit the container size

            // Add to the existing tile without stretching it
            startTile.getChildren().add(playerContainer);

        } catch (Exception e) {
            System.err.println("Error loading player pieces: " + e.getMessage());
            e.printStackTrace();

            // Fallback to colored circles if images aren't available
            createFallbackPlayerPieces();
        }
    }

    /**
     * Creates colored circles as fallback player pieces.
     * The circles are added to the start tile on the game board
     * if the player images are not available.
     */
    private void createFallbackPlayerPieces() {
        // Create colored circles as fallback pieces
        Circle player1Piece = new Circle(TILE_SIZE * 0.12);
        player1Piece.setFill(Color.RED);
        player1Piece.setStroke(Color.BLACK);
        player1Piece.setStrokeWidth(1);

        Circle player2Piece = new Circle(TILE_SIZE * 0.12);
        player2Piece.setFill(Color.BLUE);
        player2Piece.setStroke(Color.BLACK);
        player2Piece.setStrokeWidth(1);

        // Both players start at position 1
        StackPane startTile = tilesMap.get(1);

        // Use StackPane instead of HBox to overlap player pieces slightly
        StackPane playerContainer = new StackPane();

        // Add padding around the pieces to offset them slightly
        StackPane.setMargin(player1Piece, new Insets(0, 5, 0, -5));  // Right padding
        StackPane.setMargin(player2Piece, new Insets(0, -5, 0, 5));  // Left padding

        playerContainer.getChildren().addAll(player1Piece, player2Piece);
        playerContainer.setMaxSize(TILE_SIZE, TILE_SIZE);  // Limit the container size

        // Add to the existing tile without stretching it
        startTile.getChildren().add(playerContainer);
    }

    /**
     * Removes the player pieces from the given tile.
     * The player pieces are removed from the tile to prepare for the next move.
     *
     * @param player the player whose piece is to be removed
     * @param tile   the tile from which the player piece is to be removed
     */
    private void removePlayerFromTile(Player player, StackPane tile) {
        // Instead of trying to find specific player pieces, collect all elements to preserve
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
     * Adds the player pieces to the given tile.
     * The player pieces are added to the tile to reflect the current position of
     * the players.
     *
     * @param player   the player whose piece is to be added
     * @param position the position of the tile to which the player piece is to be
     *                 added
     */
    private void addPlayerToTile(Player player, int position) {
        int playerIndex = players.indexOf(player);
        Player otherPlayer = players.get(1 - playerIndex);
        StackPane tile = tilesMap.get(position);

        // Preserve labels first
        List<Label> labels = new ArrayList<>();
        for (Node node : tile.getChildren()) {
            if (node instanceof Label) {
                labels.add((Label) node);
            }
        }

        // Clear tile completely
        tile.getChildren().clear();

        // Add back labels
        tile.getChildren().addAll(labels);

        // Check if both players on same tile
        boolean bothPlayersOnTile = (otherPlayer.getTileId() == position);

        // Create container for player pieces
        StackPane playerContainer = new StackPane();
        playerContainer.setMaxSize(TILE_SIZE, TILE_SIZE);

        try {
            // Add current player
            String imagePath = playerIndex == 0 ? "/boardPieces/SindreImage.png" : "/boardPieces/StianImage.png";
            Image playerImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView playerPiece = new ImageView(playerImage);
            playerPiece.setFitHeight(TILE_SIZE * 0.35);
            playerPiece.setFitWidth(TILE_SIZE * 0.35);
            playerPiece.setPreserveRatio(true);

            // Offset position if both on same tile
            if (bothPlayersOnTile) {
                StackPane.setMargin(playerPiece, new Insets(0,
                        playerIndex == 0 ? 10 : -10, 0, playerIndex == 0 ? -10 : 10));
            }

            playerContainer.getChildren().add(playerPiece);

            // Add other player if they're on same tile
            if (bothPlayersOnTile) {
                String otherImagePath = (1 - playerIndex) == 0 ? "/boardPieces/SindreImage.png" : "/boardPieces/StianImage.png";
                Image otherImage = new Image(getClass().getResourceAsStream(otherImagePath));
                ImageView otherPiece = new ImageView(otherImage);
                otherPiece.setFitHeight(TILE_SIZE * 0.35);
                otherPiece.setFitWidth(TILE_SIZE * 0.35);
                otherPiece.setPreserveRatio(true);

                StackPane.setMargin(otherPiece, new Insets(0,
                        (1 - playerIndex) == 0 ? 10 : -10, 0, (1 - playerIndex) == 0 ? -10 : 10));

                playerContainer.getChildren().add(otherPiece);
            }

            // Add container to tile
            tile.getChildren().add(playerContainer);
        } catch (Exception e) {
            System.err.println("Error loading player images: " + e.getMessage());
            e.printStackTrace();

            // Fallback to circles
            if (bothPlayersOnTile) {
                // Create circles for both players
                Circle player1Circle = new Circle(TILE_SIZE * 0.12);
                player1Circle.setFill(Color.RED);
                player1Circle.setStroke(Color.BLACK);
                player1Circle.setStrokeWidth(1);

                Circle player2Circle = new Circle(TILE_SIZE * 0.12);
                player2Circle.setFill(Color.BLUE);
                player2Circle.setStroke(Color.BLACK);
                player2Circle.setStrokeWidth(1);

                StackPane.setMargin(player1Circle, new Insets(0, 10, 0, -10));
                StackPane.setMargin(player2Circle, new Insets(0, -10, 0, 10));

                playerContainer.getChildren().addAll(player1Circle, player2Circle);
            } else {
                // Just create circle for current player
                Circle playerCircle = new Circle(TILE_SIZE * 0.12);
                playerCircle.setFill(playerIndex == 0 ? Color.RED : Color.BLUE);
                playerCircle.setStroke(Color.BLACK);
                playerCircle.setStrokeWidth(1);

                playerContainer.getChildren().add(playerCircle);
            }

            tile.getChildren().add(playerContainer);
        }
    }

    /**
     * Animates the player movement from one tile to another.
     * The player piece is moved from the old position to the new position with an
     * animation.
     *
     * @param player        the player whose piece is to be moved
     * @param fromPosition  the old position of the player piece
     * @param toPosition    the new position of the player piece
     */
    private void animatePlayerMove(Player player, int fromPosition, int toPosition) {
        int playerIndex = players.indexOf(player);
        Player otherPlayer = players.get(1 - playerIndex);

        // Get tiles for animation
        StackPane fromTile = tilesMap.get(fromPosition);
        StackPane toTile = tilesMap.get(toPosition);

        // Remove player from old position
        removePlayerFromTile(player, fromTile);

        // If other player was on the same tile, add them back
        if (otherPlayer.getTileId() == fromPosition) {
            addPlayerToTile(otherPlayer, fromPosition);
        }

        // Create animating piece
        ImageView playerPiece;
        try {
            String imagePath = playerIndex == 0 ? "/boardPieces/SindreImage.png" : "/boardPieces/StianImage.png";
            Image playerImage = new Image(getClass().getResourceAsStream(imagePath));
            playerPiece = new ImageView(playerImage);
            playerPiece.setFitHeight(TILE_SIZE * 0.35);
            playerPiece.setFitWidth(TILE_SIZE * 0.35);
            playerPiece.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading player image: " + e.getMessage());
            e.printStackTrace();

            // Skip animation and update directly
            addPlayerToTile(player, toPosition);

            // Update game info
            gameInfoLabel.setText(player.getName() + " moved from tile " + fromPosition + " to tile " + toPosition);

            // Switch to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
            statusLabel.setText(currentPlayer.getName() + "'s turn");
            return;
        }

        // Create animation container
        StackPane animationPane = new StackPane(playerPiece);

        // Get coordinates
        Bounds fromBounds = fromTile.localToScene(fromTile.getBoundsInLocal());
        Bounds toBounds = toTile.localToScene(toTile.getBoundsInLocal());

        // Add to scene
        BorderPane root = (BorderPane) fromTile.getScene().getRoot();
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

            // Add player to new position
            addPlayerToTile(player, toPosition);

            // Update game info
            gameInfoLabel.setText(player.getName() + " moved from tile " + fromPosition + " to tile " + toPosition);

            // Switch to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
            statusLabel.setText(currentPlayer.getName() + "'s turn");
        });

        transition.play();
    }

    /**
     * Moves the player one tile forward on the game board.
     * The player piece is moved one tile forward on the game board.
     *
     * @param player the player whose piece is to be moved
     */
    private void moveOneTile(Player player) {
        int oldPosition = player.getTileId();
        int newPosition = oldPosition + 1;
        player.setTileId(newPosition);

        // Animate the player movement
        animatePlayerMove(player, oldPosition, newPosition);
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
