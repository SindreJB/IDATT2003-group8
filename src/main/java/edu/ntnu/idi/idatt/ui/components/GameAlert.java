package edu.ntnu.idi.idatt.ui.components;

import java.util.Optional;

import edu.ntnu.idi.idatt.model.GameActions;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GameAlert {

  public void showGameAlert(String title, String message, GameActions gameActions) {
    Platform.runLater(() -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);

      ButtonType restartButton = new ButtonType("Restart Game");
      ButtonType newGameButton = new ButtonType("New Game");
      ButtonType exitButton = new ButtonType("Exit");

      alert.getButtonTypes().setAll(restartButton, newGameButton, exitButton);

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent()) {
        if (result.get() == restartButton) {
          gameActions.restartGame();
        } else if (result.get() == newGameButton) {
          gameActions.startNewGame();
        } else if (result.get() == exitButton) {
          gameActions.exitGame();
        }
      }
    });
  }

}
