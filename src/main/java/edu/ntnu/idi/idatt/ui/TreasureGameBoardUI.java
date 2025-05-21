package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.factory.TreasureGameFactory;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.Player;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TreasureGameBoardUI {
    private BorderPane root;
//    private TreasureGameController controller;

    public Scene createGameScene(String boardType, Stage primaryStage, List<Player> players) throws IOException {
        // Load board from configuration
        Optional<TreasureBoard> optionalBoard = TreasureGameFactory.tryCreateBoard(boardType);
        if (!optionalBoard.isPresent()) {
            throw new IOException("Failed to load treasure board");
        }
        TreasureBoard board = optionalBoard.get();

        // Initialize controller
//        controller = new TreasureGameController(board, players);

        // Create UI elements
        root = new BorderPane();

        return new Scene(root, 1080, 920);
    }

}
