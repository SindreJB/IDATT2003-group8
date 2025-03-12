package edu.ntnu.idi.idatt.view;

import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

        // Create and set up the game board
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);

        // Add padding around the board
        BorderPane.setMargin(gameBoard, new Insets(20));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Snakes and Ladders");
        primaryStage.setScene(scene);
        primaryStage.show();
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
                                    tileNumber[0]++;
                                });
                    } else {
                        IntStream.range(0, GRID_COLS)
                                .forEach(col -> {
                                    StackPane tile = createTile(tileNumber[0]);
                                    tile.getStyleClass().add("styled-tile");
                                    gridPane.add(tile, col, row);
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

    /**
     * The main entry point for the Java application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
