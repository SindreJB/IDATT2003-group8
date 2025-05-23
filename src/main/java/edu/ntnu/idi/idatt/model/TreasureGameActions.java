package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.ui.TreasureGameBoardUI;
import edu.ntnu.idi.idatt.ui.pages.MainMenu;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class TreasureGameActions implements GameActions {    private final TreasureGameBoardUI gameBoard;

    /**
     * Constructs a new TreasureGameActions with a reference to the game board.
     * 
     * @param gameBoard The treasure game board that will be controlled
     * @throws NullPointerException if gameBoard is null
     */
    public TreasureGameActions(TreasureGameBoardUI gameBoard) {
        if (gameBoard == null) {
            throw new NullPointerException("gameBoard cannot be null");
        }
        this.gameBoard = gameBoard;
    }

    @Override
    public void restartGame() {
        try {
            gameBoard.resetGame();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Game Initialization Error");
            alert.setContentText("An error occurred while restarting the game: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void startNewGame() {
        try {
            // Get the current stage from the game board's scene
            Stage stage = (Stage) gameBoard.getRoot().getScene().getWindow();

            // Create a new MainMenu instance
            MainMenu mainMenu = new MainMenu();

            // Create the main menu scene
            Scene mainMenuScene = mainMenu.createMainMenuScene(stage);

            // Set the main menu scene on the stage
            stage.setScene(mainMenuScene);
            stage.setTitle("Boardgame Menu");

            // Adjust stage size to match main menu
            stage.setWidth(600);
            stage.setHeight(400);
            stage.centerOnScreen();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Unable to return to main menu: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void exitGame() {
        Platform.exit();
    }
}