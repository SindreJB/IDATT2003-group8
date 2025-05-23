package edu.ntnu.idi.idatt.ui.components;

import java.util.List;

import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreatePlayer {
  public static void display() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Create New Player");
    window.setWidth(300);
    window.setHeight(400);
    window.setResizable(false);

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(8);
    grid.setHgap(10);

    // Name input
    Label nameLabel = new Label("Player Name:");
    GridPane.setConstraints(nameLabel, 0, 0);

    TextField nameInput = new TextField();
    nameInput.setPromptText("Enter name");
    GridPane.setConstraints(nameInput, 1, 0);

    // Color picker
    Label colorLabel = new Label("Player Color:");
    GridPane.setConstraints(colorLabel, 0, 1);

    ColorPicker colorPicker = new ColorPicker(Color.RED);
    GridPane.setConstraints(colorPicker, 1, 1);    // Buttons
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    
    // Style buttons
    String primaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
                               "-fx-background-color: #1976d2; -fx-text-fill: white;";
    String secondaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
                                 "-fx-background-color: #424242; -fx-text-fill: white;";
    
    saveButton.setStyle(primaryButtonStyle);
    cancelButton.setStyle(secondaryButtonStyle);
    
    // Add hover effects
    saveButton.setOnMouseEntered(e -> saveButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    saveButton.setOnMouseExited(e -> saveButton.setStyle(primaryButtonStyle));
    
    cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(secondaryButtonStyle.replace("#424242", "#323232")));
    cancelButton.setOnMouseExited(e -> cancelButton.setStyle(secondaryButtonStyle));

    saveButton.setOnAction(e -> {
      if (nameInput.getText().trim().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Player name cannot be empty!");
        alert.showAndWait();
        return;
      }

      try {
        // Convert color to hex format
        Color color = colorPicker.getValue();
        String colorHex = String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));

        Player player = new Player(nameInput.getText(), colorHex, 0);
        savePlayersToCsv(List.of(player));

        window.close();
      } catch (Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Save Failed");
        alert.setContentText("Failed to save player: " + ex.getMessage());
        alert.showAndWait();
      }
    });

    cancelButton.setOnAction(e -> window.close());

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(saveButton, cancelButton);
    GridPane.setConstraints(buttonBox, 1, 2);

    grid.getChildren().addAll(nameLabel, nameInput, colorLabel, colorPicker, buttonBox);

    Scene scene = new Scene(grid);
    window.setScene(scene);
    window.showAndWait();
  }

  /**
   * Opens a file chooser dialog to save sample players to a CSV file
   */
  private static void savePlayersToCsv(List<Player> players) {

    String savePath = "data/players/players.csv";
    try {
      CsvHandler.savePlayersToCsv(players, savePath);

    } catch (FileWriteException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Save Failed");
      alert.setContentText("Failed to save players: " + e.getMessage());
      alert.showAndWait();
    }
  }
}
