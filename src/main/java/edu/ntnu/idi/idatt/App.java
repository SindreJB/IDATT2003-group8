package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.view.Board;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board();
        board.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}