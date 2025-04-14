package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.ui.LadderGameBoard;
import edu.ntnu.idi.idatt.ui.Page;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        LadderGameBoard board = new LadderGameBoard();
        Page page = new Page();
        page.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}