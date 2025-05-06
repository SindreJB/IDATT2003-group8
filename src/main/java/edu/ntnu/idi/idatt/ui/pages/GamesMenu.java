package edu.ntnu.idi.idatt.ui.pages;

import edu.ntnu.idi.idatt.ui.LadderGameBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GamesMenu {

  private BorderPane root;

  public Scene createGamesMenuScene(Stage primaryStage) {
    root = new BorderPane();
    root.setStyle("-fx-background-color: #F0EFEB;");

    // Create header
    Label titleLabel = new Label("Select Game Type");
    titleLabel.getStyleClass().add("title-label");

    // Create game selection buttons
    Button standardButton = createGameButton("Standard", "Classic Snakes and Ladders board",
        primaryStage, "standard");
    Button emptyButton = createGameButton("Empty", "Board with no snakes or ladders",
        primaryStage, "empty");
    Button customButton = createGameButton("Custom", "Custom board configuration",
        primaryStage, "custom");
    Button wormholeButton = createGameButton("Wormhole", "Board with wormholes, snakes, and ladders",
        primaryStage, "wormhole");

    // Back button
    Button backButton = new Button("Back to Menu");
    backButton.getStyleClass().add("menu-button");
    backButton.setOnAction(e -> {
      MainMenu mainMenu = new MainMenu();
      Scene mainMenuScene = mainMenu.createMainMenuScene(primaryStage);
      primaryStage.setScene(mainMenuScene);
    });

    // Create layout
    VBox buttonContainer = new VBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.getChildren().addAll(titleLabel, standardButton, emptyButton, customButton, wormholeButton,
        backButton);
    buttonContainer.setPadding(new Insets(50));

    root.setCenter(buttonContainer);

    Scene scene = new Scene(root, 800, 600);
    // Fix the stylesheet path
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

    // Set action to launch the game with selected board type
    button.setOnAction(e -> {
      LadderGameBoard gameBoard = new LadderGameBoard();
      Scene gameScene = gameBoard.createGameScene(boardType, primaryStage);
      primaryStage.setScene(gameScene);
      primaryStage.setTitle("Snakes and Ladders - " + name);
    });

    return button;
  }
}