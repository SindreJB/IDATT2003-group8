package edu.ntnu.idi.idatt.ui.pages;

import edu.ntnu.idi.idatt.ui.components.PlayerSelectionModal;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GamesMenu {

  private VBox root;

  public Scene createGamesMenuScene(Stage primaryStage) {
    root = new VBox(30);
    root.setStyle("-fx-alignment: center; -fx-background-color: lightblue;");

    // Create header
    Label titleLabel = new Label("Select Game Type");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-padding: 8;");

    // Create game selection buttons
    Button treasureButton = createGameButton("Treasure Hunt", "Find the treasure on the board",
        primaryStage, "treasure");
    Button standardButton = createGameButton("Standard", "Classic Snakes and Ladders board",
        primaryStage, "standard");
    Button wormholeButton = createGameButton("Wormhole", "Board with wormholes",
        primaryStage, "wormhole");

    // Set button styles
    String primaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
        "-fx-background-color: #1976d2; -fx-text-fill: white; -fx-pref-width: Infinity;";

    standardButton.setStyle(primaryButtonStyle);
    treasureButton.setStyle(primaryButtonStyle);
    wormholeButton.setStyle(primaryButtonStyle);

    // Add hover effects to primary buttons
    treasureButton.setOnMouseEntered(e -> treasureButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    treasureButton.setOnMouseExited(e -> treasureButton.setStyle(primaryButtonStyle));

    standardButton.setOnMouseEntered(e -> standardButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    standardButton.setOnMouseExited(e -> standardButton.setStyle(primaryButtonStyle));

    wormholeButton.setOnMouseEntered(e -> wormholeButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    wormholeButton.setOnMouseExited(e -> wormholeButton.setStyle(primaryButtonStyle));

    // Back button
    Button backButton = new Button("Back to Menu");
    String destructiveButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
        "-fx-background-color: #f44336; -fx-text-fill: white; -fx-pref-width: Infinity;";

    backButton.setStyle(destructiveButtonStyle);

    // Add hover and pressed effects to destructive button
    backButton.setOnMouseEntered(e -> backButton.setStyle(destructiveButtonStyle.replace("#f44336", "#d32f2f")));
    backButton.setOnMouseExited(e -> backButton.setStyle(destructiveButtonStyle));
    backButton.setOnMousePressed(e -> backButton.setStyle(destructiveButtonStyle.replace("#f44336", "#c62828")));
    backButton.setOnMouseReleased(e -> {
      if (backButton.isHover()) {
        backButton.setStyle(destructiveButtonStyle.replace("#f44336", "#d32f2f"));
      } else {
        backButton.setStyle(destructiveButtonStyle);
      }
    });

    backButton.setOnAction(e -> {
      MainMenu mainMenu = new MainMenu();
      Scene mainMenuScene = mainMenu.createMainMenuScene(primaryStage);
      primaryStage.setScene(mainMenuScene);
    });

    // Create layout
    VBox buttonContainer = new VBox(20);
    buttonContainer.setStyle("-fx-alignment: center; -fx-max-width: 200px; -fx-pref-height: 100%; " +
        "-fx-padding: 16; -fx-spacing: 8;");
    VBox.setMargin(buttonContainer, new Insets(16, 0, 0, 0)); // For margin-top

    buttonContainer.getChildren().addAll(treasureButton, standardButton, wormholeButton, backButton);
    root.getChildren().addAll(titleLabel, buttonContainer);

    // Create scene without stylesheet
    Scene scene = new Scene(root);
    return scene;
  }

  private Button createGameButton(String name, String description, Stage primaryStage, String boardType) {
    Button button = new Button(name);

    // Set tooltip with description
    button.setTooltip(new javafx.scene.control.Tooltip(description));

    // Set action to open the player selection modal
    button.setOnAction(e -> {
      // Show player selection modal
      PlayerSelectionModal playerModal = new PlayerSelectionModal(boardType, primaryStage);
      playerModal.showModal();
    });

    return button;
  }
}