package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.factory.BoardFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
import edu.ntnu.idi.idatt.ui.components.GamePiece;
import edu.ntnu.idi.idatt.ui.components.InfoTable;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LadderGameBoard class represents the main application for the Snakes and
 * Ladders
 * game. It extends the JavaFX Application class and sets up the game board UI.
 */
public class LadderGameBoard extends Application {

    private static final int TILE_SIZE = 60;

    private Board board;
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
    private String selectedBoardName = "classic";

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

        // Set up board selector
        HBox boardSelectorBox = createBoardSelector();

        // Initialize board from factory - start with classic board by default
        initializeBoard();

        // Initialize players (for demonstration)
        initializePlayers();

        // Initialize GamePiece after players are created
        gamePiece = new GamePiece(TILE_SIZE, players);

        // Create and set up the game board
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);

        // Add padding around the board
        BorderPane.setMargin(gameBoard, new Insets(20));

        // Add board selector to top
        root.setTop(boardSelectorBox);

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
     * Creates a board selector component.
     * 
     * @return HBox containing the board selector
     */
    private HBox createBoardSelector() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        Label label = new Label("Board:");
        ComboBox<String> boardSelector = new ComboBox<>();
        Button loadButton = new Button("Load Board");

        // Populate board selector
        try {
            List<String> boards = BoardFactory.getAvailableBoards();
            if (boards.isEmpty()) {
                boardSelector.getItems().add("classic");
            } else {
                boardSelector.getItems().addAll(boards);
            }
            boardSelector.getSelectionModel().select(0);
        } catch (IOException e) {
            showErrorAlert("Error loading boards", "Could not load board configurations: " + e.getMessage());
            boardSelector.getItems().add("classic");
            boardSelector.getSelectionModel().select(0);
        }

        // Action for load button
        loadButton.setOnAction(e -> {
            String selected = boardSelector.getValue();
            if (selected != null && !selected.isEmpty()) {
                selectedBoardName = selected;
                reloadBoard();
            }
        });

        box.getChildren().addAll(label, boardSelector, loadButton);
        return box;
    }

    /**
     * Reloads the board with the currently selected board configuration.
     */
    private void reloadBoard() {
        // Reset game state
        players.forEach(player -> player.setTileId(1));
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);

        // Initialize new board
        initializeBoard();

        // Recreate game board UI
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);
        BorderPane.setMargin(gameBoard, new Insets(20));

        // Reset UI
        statusLabel.setText(currentPlayer.getName() + "'s turn");
        gameInfoLabel.setText("Game restarted. Roll the dice to begin.");

        // Setup player pieces on the board
        gamePiece.setupPlayerPieces(tilesMap.get(1));
    }

    /**
     * Initializes the game board from the BoardFactory.
     */
    private void initializeBoard() {
        try {
            board = BoardFactory.createBoard(selectedBoardName);
        } catch (IOException e) {
            showErrorAlert("Error loading board", "Could not load board configuration: " + e.getMessage());
            try {
                board = BoardFactory.createClassicBoard();
            } catch (Exception ex) {
                board = new Board(9, 10);
            }
        }
    }

    /**
     * Initializes players for the game.
     * This method attempts to load players from a CSV file, or creates default
     * players if loading fails.
     */
    private void initializePlayers() {
        players.clear();

        // Try to load players from CSV
        try {
            List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv("players.csv");
            if (!loadedPlayers.isEmpty()) {
                players.addAll(loadedPlayers);
            } else {
                // Add default players if none were loaded
                players.add(new Player("Player 1", 1));
                players.add(new Player("Player 2", 1));
            }
        } catch (IOException e) {
            // Add default players if loading failed
            players.add(new Player("Player 1", 1));
            players.add(new Player("Player 2", 1));
        }

        // Set current player
        currentPlayer = players.get(0);
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
        int newPosition = Math.min(oldPosition + diceValue, board.getNumberOfTiles());
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
     * snake-like pattern. The tiles are styled with the "styled-tile" CSS class.
     *
     * @return a GridPane representing the game board with tiles arranged in a
     *         snake-like pattern.
     */
    private GridPane createGameBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        tilesMap.clear();

        // Create tiles based on the board configuration
        int rows = board.getRows();
        int cols = board.getColumns();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Find the tile at this position
                Tile tile = null;
                try {
                    tile = board.getTileAt(col, row);
                } catch (IllegalArgumentException e) {
                    // Skip if no tile at this position
                    continue;
                }

                if (tile != null) {
                    StackPane tilePane = createTile(tile.getNumber());
                    tilePane.getStyleClass().add("styled-tile");
                    gridPane.add(tilePane, col, row);
                    tilesMap.put(tile.getNumber(), tilePane);
                }
            }
        }

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

            // Check if tile has an action
            Tile landedTile = board.getTile(toPosition);
            handleTileAction(player, landedTile, toPosition);

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

            // Check if tile has an action
            Tile landedTile = board.getTile(toPosition);
            handleTileAction(player, landedTile, toPosition);

            // Switch to next player
            switchToNextPlayer();

            // Re-enable roll button
            infoTable.setRollEnabled(true);
        });

        transition.play();
    }

    /**
     * Handles any actions associated with a tile when a player lands on it.
     * 
     * @param player   The player who landed on the tile
     * @param tile     The tile landed on
     * @param position The position of the tile
     */
    private void handleTileAction(Player player, Tile tile, int position) {
        if (tile != null && tile.getAction() != null) {
            gameInfoLabel.setText(gameInfoLabel.getText() + "\n" + tile.getAction().getDescription());

            // Remember the old position
            int oldPosition = position;

            // Perform the action
            tile.performAction(player);

            // If the player's position changed, animate that movement too
            int newPosition = player.getTileId();
            if (newPosition != oldPosition) {
                StackPane fromTile = tilesMap.get(oldPosition);
                StackPane toTile = tilesMap.get(newPosition);

                // Remove player from old position
                removePlayerFromTile(fromTile);

                // Add to new position
                gamePiece.addPlayerToTile(player, newPosition, toTile);

                gameInfoLabel.setText(gameInfoLabel.getText() + "\n" +
                        player.getName() + " moved to tile " + newPosition);
            }
        }
    }

    /**
     * Switches to the next player
     */
    private void switchToNextPlayer() {
        // Check for victory
        if (currentPlayer.getTileId() >= board.getNumberOfTiles()) {
            handleVictory();
            return;
        }

        // Update player index and current player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);

        // Update status label
        statusLabel.setText(currentPlayer.getName() + "'s turn");
    }

    /**
     * Handles the victory condition
     */
    private void handleVictory() {
        // Disable roll button
        infoTable.setRollEnabled(false);

        // Update game info
        gameInfoLabel.setText(currentPlayer.getName() + " wins the game!");
        statusLabel.setText("Game Over");

        // Show victory dialog
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("We have a winner!");
        alert.setContentText(currentPlayer.getName() + " has won the game!");
        alert.showAndWait();
    }

    /**
     * Shows an error alert.
     * 
     * @param title   The title of the alert
     * @param message The message to display
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
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