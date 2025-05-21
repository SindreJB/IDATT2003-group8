package edu.ntnu.idi.idatt.ui.pages;

import edu.ntnu.idi.idatt.ui.components.PlayerSelectionModal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GamesMenu {

  private VBox root;

  public Scene createGamesMenuScene(Stage primaryStage) {
    root = new VBox(30);
    root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    // Create header
    Label titleLabel = new Label("Select Game Type");
    titleLabel.getStyleClass().addAll("text-2xl", "font-bold", "text-black", "p-2");

    // Create game selection buttons
    Button treasureButton = createGameButton("Treasure Hunt", "Find the treasure on the board",
        primaryStage, "treasure");
    Button standardButton = createGameButton("Standard", "Classic Snakes and Ladders board",
        primaryStage, "standard");
    Button emptyButton = createGameButton("Empty", "Board with no snakes or ladders",
        primaryStage, "empty");
    Button customButton = createGameButton("Custom", "Custom board configuration",
        primaryStage, "custom");
    Button wormholeButton = createGameButton("Wormhole", "Board with wormholes",
        primaryStage, "wormhole");

    // Set button styles
    standardButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    emptyButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    customButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    treasureButton.getStyleClass().addAll("btn", "btn-primary", "w-full");
    wormholeButton.getStyleClass().addAll("btn", "btn-primary", "w-full");

    // Back button
    Button backButton = new Button("Back to Menu");
    backButton.getStyleClass().addAll("btn", "btn-destructive", "w-full");
    backButton.setOnAction(e -> {
      MainMenu mainMenu = new MainMenu();
      Scene mainMenuScene = mainMenu.createMainMenuScene(primaryStage);
      primaryStage.setScene(mainMenuScene);
    });

    // Create layout
    VBox buttonContainer = new VBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.getChildren().addAll(treasureButton, standardButton, emptyButton, customButton, wormholeButton,
        backButton);
    buttonContainer.getStyleClass().addAll(
        "items-center", "w-200", "h-full", "mt-4", "p-4", "space-y-2");
    root.getChildren().addAll(titleLabel, buttonContainer);
    root.setAlignment(Pos.CENTER);

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

    return scene;
  }

  private Button createGameButton(String name, String description, Stage primaryStage, String boardType) {
    Button button = new Button(name);
    button.getStyleClass().add("game-select-button");
    button.setMaxWidth(300);
    button.setMinHeight(60);

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