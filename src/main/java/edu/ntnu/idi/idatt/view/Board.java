package edu.ntnu.idi.idatt.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    primaryStage.setTitle("Snakes and Ladders");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

//  private GridPane createGameBoard() {
//    GridPane gridPane = new GridPane();
//    gridPane.setAlignment(Pos.CENTER);
//    gridPane.setHgap(2);
//    gridPane.setVgap(2);
//
//    // Create tiles numbered from 90 down to 1
//    int tileNumber = 90;
//    for (int row = 0; row < GRID_ROWS; row++) {
//      for (int col = 0; col < GRID_COLS; col++) {
//        StackPane tile = createTile(tileNumber);
//        gridPane.add(tile, col, row);
//        tileNumber--;
//      }
//    }
//
//    return gridPane;
//  }

  private GridPane createGameBoard() {
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(2);
    gridPane.setVgap(2);

    // Create tiles in a snake-like pattern
    int tileNumber = 1;
    for (int row = GRID_ROWS - 1; row >= 0; row--) {
      if (row % 2 == 0) {
        for (int col = GRID_COLS - 1; col >= 0; col--) {
          StackPane tile = createTile(tileNumber);
          gridPane.add(tile, col, row);
          tileNumber++;
        }
      } else {
        for (int col = 0; col < GRID_COLS; col++) {
          StackPane tile = createTile(tileNumber);
          gridPane.add(tile, col, row);
          tileNumber++;
        }
      }
    }

    return gridPane;
  }

  private StackPane createTile(int number) {
    StackPane tile = new StackPane();

    // Create tile background
    Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE);
    background.setFill(Color.WHITE);
    background.setStroke(Color.BLACK);

    // Create tile number
    Text text = new Text(String.valueOf(number));
    text.setStyle("-fx-font-size: 16px;");

    // Add background and number to tile
    tile.getChildren().addAll(background, text);

    return tile;
  }

  public static void main(String[] args) {
    launch(args);
  }
}