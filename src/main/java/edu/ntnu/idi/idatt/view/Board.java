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

public class Board extends Application {

    private static final int TILE_SIZE = 60;
    private static final int GRID_ROWS = 10;
    private static final int GRID_COLS = 9;

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

    private GridPane createGameBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);

        // Create tiles in a snake-like pattern
        int[] tileNumber = {1};
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

    public static void main(String[] args) {
        launch(args);
    }
}
