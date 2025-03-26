package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
        this.diceContainer = diceContainer;
        this.gameInfoLabel = gameInfoLabel;

        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setAlignment(Pos.TOP_CENTER);

        statusLabel = new Label(currentPlayer.getName() + "'s turn");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button rollButton = new Button("Roll Dice");
        // rollButton.setOnAction(e -> rollDice()); TODO: Implement rollDice method

        // Create a space for displaying dice
        HBox diceContainer = new HBox(10);
        diceContainer.setAlignment(Pos.CENTER);
        diceContainer.setPrefHeight(60);
        diceContainer.setPrefWidth(100);
        diceContainer.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f8f8f8;");

        // Create a text area for game info
        Label gameInfoLabel = new Label("Game information will appear here");
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

        // Create tile background
        // Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE);
        // Create tile number
        Label text = new Label(String.valueOf(number));
        text.getStyleClass().add("styled-text");

        // Add background and number to tile
        tile.getChildren().addAll(text);
        // tile.getStyleClass().add("styled-tile");

        return tile;
    }

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

    private void updatePlayerPosition(Player player, int oldPosition, int newPosition) {
        // Remove player piece from old position if it exists
        if (tilesMap.containsKey(oldPosition)) {
            // Find and remove only this player's piece
            // In a more complete implementation, you would need to identify each player's piece uniquely
        }

        // Add player piece to new position
        if (tilesMap.containsKey(newPosition)) {
            // Similar to setupPlayerPieces, but for a specific player
            // This would be implemented when you're ready for the roll function
        }
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
