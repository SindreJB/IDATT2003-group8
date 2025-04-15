package edu.ntnu.idi.idatt;

import java.io.IOException;

import edu.ntnu.idi.idatt.factory.BoardFactory;
import edu.ntnu.idi.idatt.ui.Page;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Main application class that initializes the game.
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize default boards if they don't exist
        try {
            BoardFactory.generateDefaultBoards();
        } catch (IOException e) {
            showErrorAlert("Error initializing board configurations",
                    "Could not initialize default boards: " + e.getMessage());
        }

        // Start the main menu page
        Page page = new Page();
        page.start(primaryStage);
    }

    /**
     * Helper method to show error dialogs
     * 
     * @param title   The title of the error dialog
     * @param message The error message to display
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The main entry point for the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}