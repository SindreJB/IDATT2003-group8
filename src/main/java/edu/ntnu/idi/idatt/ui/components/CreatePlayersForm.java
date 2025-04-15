package edu.ntnu.idi.idatt.ui.components;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreatePlayersForm {
  public static void display() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Create Players");
    window.setMinWidth(400);
    window.setMinHeight(300);

    Label titleLabel = new Label("Create New Players");
    titleLabel.getStyleClass().addAll("text-xl", "font-bold");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(20));
    gridPane.setAlignment(Pos.CENTER);

    // Player 1 details
    Label player1Label = new Label("Player 1:");
    TextField player1NameField = new TextField();
    player1NameField.setPromptText("Enter name");
    ComboBox<String> player1PieceCombo = new ComboBox<>();
    player1PieceCombo.getItems().addAll("X", "O", "Triangle", "Square", "Star");
    player1PieceCombo.setValue("X");
    player1PieceCombo.setPromptText("Select piece");

    // Player 2 details
    Label player2Label = new Label("Player 2:");
    TextField player2NameField = new TextField();
    player2NameField.setPromptText("Enter name");
    ComboBox<String> player2PieceCombo = new ComboBox<>();
    player2PieceCombo.getItems().addAll("X", "O", "Triangle", "Square", "Star");
    player2PieceCombo.setValue("O");
    player2PieceCombo.setPromptText("Select piece");

    // Add option for more players
    Button addPlayerButton = new Button("Add Another Player");

    // Setup grid layout
    gridPane.add(player1Label, 0, 0);
    gridPane.add(player1NameField, 1, 0);
    gridPane.add(player1PieceCombo, 2, 0);

    gridPane.add(player2Label, 0, 1);
    gridPane.add(player2NameField, 1, 1);
    gridPane.add(player2PieceCombo, 2, 1);

    // Setup additional player rows (initially hidden)
    List<TextField> additionalNameFields = new ArrayList<>();
    List<ComboBox<String>> additionalPieceFields = new ArrayList<>();

    addPlayerButton.setOnAction(e -> {
      int row = gridPane.getRowCount();
      if (row < 6) { // Limit to 6 players total
        Label playerLabel = new Label("Player " + (row + 1) + ":");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");
        ComboBox<String> pieceCombo = new ComboBox<>();
        pieceCombo.getItems().addAll("X", "O", "Triangle", "Square", "Star");
        pieceCombo.setPromptText("Select piece");

        additionalNameFields.add(nameField);
        additionalPieceFields.add(pieceCombo);

        gridPane.add(playerLabel, 0, row);
        gridPane.add(nameField, 1, row);
        gridPane.add(pieceCombo, 2, row);

        if (row == 5) {
          addPlayerButton.setDisable(true);
        }
      }
    });

    // Buttons
    Button saveButton = new Button("Save Players");
    Button cancelButton = new Button("Cancel");

    saveButton.getStyleClass().addAll("btn", "btn-primary");
    cancelButton.getStyleClass().addAll("btn", "btn-secondary");
    addPlayerButton.getStyleClass().addAll("btn", "btn-secondary");

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(saveButton, cancelButton);

    // Action handlers
    saveButton.setOnAction(e -> {
      // Collect all player data
      List<Player> players = new ArrayList<>();

      if (!player1NameField.getText().isEmpty()) {
        players.add(new Player(player1NameField.getText(), player1PieceCombo.getValue(), 1));
      }

      if (!player2NameField.getText().isEmpty()) {
        players.add(new Player(player2NameField.getText(), player2PieceCombo.getValue(), 1));
      }

      // Add additional players
      for (int i = 0; i < additionalNameFields.size(); i++) {
        if (!additionalNameFields.get(i).getText().isEmpty()) {
          players.add(new Player(
              additionalNameFields.get(i).getText(),
              additionalPieceFields.get(i).getValue(),
              1));
        }
      }

      if (players.isEmpty()) {
        showAlert("Please create at least one player", window);
        return;
      }

      // Save players
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Players");
      fileChooser.getExtensionFilters().add(
          new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
      fileChooser.setInitialDirectory(new File("src/main/resources/data"));
      fileChooser.setInitialFileName("players.csv");

      File file = fileChooser.showSaveDialog(window);
      if (file != null) {
        try {
          CsvHandler.savePlayersToCsv(players, file.getPath());
          showAlert("Successfully saved " + players.size() + " players to " + file.getName(), window);
          window.close();
        } catch (IOException ex) {
          showAlert("Error saving players: " + ex.getMessage(), window);
          ex.printStackTrace();
        }
      }
    });

    cancelButton.setOnAction(e -> window.close());

    // Layout
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(20));
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(titleLabel, gridPane, addPlayerButton, buttonBox);

    Scene scene = new Scene(layout);
    scene.getStylesheets()
        .add(CreatePlayersForm.class.getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

    window.setScene(scene);
    window.showAndWait();
  }

  private static void showAlert(String message, Stage owner) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.initOwner(owner);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
