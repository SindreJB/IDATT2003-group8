package edu.ntnu.idi.idatt.ui.components;

import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;
import edu.ntnu.idi.idatt.ui.LadderGameBoardUI;
import edu.ntnu.idi.idatt.ui.TreasureGameBoardUI;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Modal dialog for selecting players and game pieces
 */
public class PlayerSelectionModal {

  private final String boardType;
  private final Stage primaryStage;
  private final List<PlayerConfig> availablePieces = new ArrayList<>();

  // Player selection components
  private VBox playerCountPanel;
  private VBox playerSelectionPanel;
  private Spinner<Integer> playerCountSpinner;
  private final List<ComboBox<String>> playerSelectors = new ArrayList<>();

  /**
   * Represents a player configuration from CSV
   */
  private static class PlayerConfig {
    String name;
    String pieceType;

    public PlayerConfig(String name, String pieceType) {
      this.name = name;
      this.pieceType = pieceType;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  /**
   * Creates a new player selection modal
   * 
   * @param boardType    The selected board type
   * @param primaryStage The primary application stage
   */
  public PlayerSelectionModal(String boardType, Stage primaryStage) {
    this.boardType = boardType;
    this.primaryStage = primaryStage;

    // Load available pieces from CSV
    loadPlayerPieces();
  }

  /**
   * Loads player pieces from the CSV file
   */
  private void loadPlayerPieces() {
    try {
      List<Player> csvPlayers = CsvHandler.loadPlayersFromCsv("src/main/resources/players/players.csv");

      for (Player player : csvPlayers) {
        availablePieces.add(new PlayerConfig(player.getName(), player.getPieceType()));
      }

      if (availablePieces.isEmpty()) {
        // Fallback option if CSV is empty

        availablePieces.add(new PlayerConfig("Sindre", "#ffffff"));
        availablePieces.add(new PlayerConfig("Stian", "#000000"));
      }
    } catch (FileReadException e) {
      availablePieces.add(new PlayerConfig("Sindre", "#ffffff"));
      availablePieces.add(new PlayerConfig("Stian", "#000000"));
    }
  }

  /**
   * Shows the player selection modal
   */
  public void showModal() {
    Stage window = new Stage();
    window.setWidth(300);
    window.setHeight(400);
    window.setResizable(false);
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Player Selection");

    // Create panels
    createPlayerCountPanel(window);
    createPlayerSelectionPanel(window);

    // Show the player count panel
    VBox mainLayout = new VBox(20);
    mainLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
    mainLayout.getChildren().add(playerCountPanel);

    Scene scene = new Scene(mainLayout);
    // Remove stylesheet
    // scene.getStylesheets().add(getClass().getResource("/edu/ntnu/idi/idatt/view/styles.css").toExternalForm());

    window.setScene(scene);
    window.showAndWait();
  }

  /**
   * Creates the panel for selecting the number of players
   * 
   * @param window The modal window
   */
  private void createPlayerCountPanel(Stage window) {
    playerCountPanel = new VBox(20);
    playerCountPanel.setStyle("-fx-alignment: center;");

    Label titleLabel = new Label("How many players?");
    titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    // Player count spinner (1-5 players)
    playerCountSpinner = new Spinner<>();
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 2);
    playerCountSpinner.setValueFactory(valueFactory);
    playerCountSpinner.setPrefWidth(80);

    // Continue button
    Button continueButton = new Button("Continue");
    String primaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
        "-fx-background-color: #1976d2; -fx-text-fill: white;";
    continueButton.setStyle(primaryButtonStyle);

    // Add hover effect
    continueButton.setOnMouseEntered(e -> continueButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    continueButton.setOnMouseExited(e -> continueButton.setStyle(primaryButtonStyle));

    continueButton.setOnAction(e -> {
      // Switch to player selection panel
      VBox mainLayout = (VBox) window.getScene().getRoot();
      mainLayout.getChildren().clear();

      // Update player selection panel for the chosen player count
      updatePlayerSelectionPanel(playerCountSpinner.getValue());

      mainLayout.getChildren().add(playerSelectionPanel);
    });

    playerCountPanel.getChildren().addAll(titleLabel, playerCountSpinner, continueButton);
  }

  /**
   * Creates the panel for selecting player pieces
   * 
   * @param window The modal window
   */
  private void createPlayerSelectionPanel(Stage window) {
    playerSelectionPanel = new VBox(20);
    playerSelectionPanel.setStyle("-fx-alignment: center;");

    Label titleLabel = new Label("Select Player Pieces");
    titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(15);
    playerGrid.setVgap(15);
    playerGrid.setAlignment(Pos.CENTER);

    // Create up to 5 player selectors
    for (int i = 0; i < 5; i++) {
      Label playerLabel = new Label("Player " + (i + 1) + ":");
      ComboBox<String> pieceSelector = new ComboBox<>();

      // Add all available pieces as options
      for (PlayerConfig piece : availablePieces) {
        pieceSelector.getItems().add(piece.name);
      }

      // Default selection for first two players
      if (i < availablePieces.size()) {
        pieceSelector.setValue(availablePieces.get(i).name);
      }

      playerGrid.add(playerLabel, 0, i);
      playerGrid.add(pieceSelector, 1, i);

      playerSelectors.add(pieceSelector);
    }

    // Buttons
    String primaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
        "-fx-background-color: #1976d2; -fx-text-fill: white;";
    Button startButton = new Button("Start Game");
    startButton.setStyle(primaryButtonStyle);

    // Add hover effect
    startButton.setOnMouseEntered(e -> startButton.setStyle(primaryButtonStyle.replace("#1976d2", "#1565c0")));
    startButton.setOnMouseExited(e -> startButton.setStyle(primaryButtonStyle));

    String secondaryButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
        "-fx-background-color: #424242; -fx-text-fill: white;";
    Button backButton = new Button("Back");
    backButton.setStyle(secondaryButtonStyle);

    // Add hover effect
    backButton.setOnMouseEntered(e -> backButton.setStyle(secondaryButtonStyle.replace("#424242", "#323232")));
    backButton.setOnMouseExited(e -> backButton.setStyle(secondaryButtonStyle));

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, startButton);

    // Button actions
    backButton.setOnAction(e -> {
      // Go back to player count selection
      VBox mainLayout = (VBox) window.getScene().getRoot();
      mainLayout.getChildren().clear();
      mainLayout.getChildren().add(playerCountPanel);
    });

    startButton.setOnAction(e -> {
      if (validatePlayerSelections()) {
        // Create players and start the game
        List<Player> selectedPlayers = createSelectedPlayers();
        startGame(selectedPlayers);
        window.close();
      }
    });

    playerSelectionPanel.getChildren().addAll(titleLabel, playerGrid, buttonBox);
  }

  /**
   * Updates the player selection panel based on the chosen player count
   * 
   * @param playerCount The number of players
   */
  private void updatePlayerSelectionPanel(int playerCount) {
    // Show/hide the appropriate number of player selectors
    GridPane grid = (GridPane) playerSelectionPanel.getChildren().get(1);

    // Make visible rows based on player count, hide others
    for (int i = 0; i < 5; i++) {
      boolean visible = i < playerCount;
      grid.getChildren().get(i * 2).setVisible(visible); // Label
      grid.getChildren().get(i * 2 + 1).setVisible(visible); // ComboBox

      // Default selection
      if (visible && i < availablePieces.size()) {
        playerSelectors.get(i).setValue(availablePieces.get(i).name);
      }
    }
  }

  /**
   * Validates that all player selections are valid
   * 
   * @return True if all selections are valid
   */
  private boolean validatePlayerSelections() {
    int playerCount = playerCountSpinner.getValue();
    List<String> selectedPieces = new ArrayList<>();

    for (int i = 0; i < playerCount; i++) {
      String piece = playerSelectors.get(i).getValue();

      if (piece == null || piece.isEmpty()) {
        showAlert("Please select a piece for Player " + (i + 1));
        return false;
      }

      if (selectedPieces.contains(piece)) {
        showAlert("Each player must have a unique piece. " + piece + " is already selected.");
        return false;
      }

      selectedPieces.add(piece);
    }

    return true;
  }

  /**
   * Creates Player objects based on selections
   * 
   * @return List of selected Player objects
   */
  private List<Player> createSelectedPlayers() {
    List<Player> players = new ArrayList<>();
    int playerCount = playerCountSpinner.getValue();

    for (int i = 0; i < playerCount; i++) {
      String pieceName = playerSelectors.get(i).getValue();
      String pieceType = getPieceType(pieceName);

      // Use piece name as player name, starting position 1
      Player player = new Player(pieceName, pieceType, 1);
      players.add(player);
    }

    return players;
  }

  /**
   * Gets the piece type for a given piece name
   * 
   * @param pieceName The name of the piece
   * @return The piece type
   */
  private String getPieceType(String pieceName) {
    for (PlayerConfig config : availablePieces) {
      if (config.name.equals(pieceName)) {
        return config.pieceType;
      }
    }

    // Fallback
    return pieceName;
  }

  /**
   * Shows an alert message
   * 
   * @param message The message to display
   */
  private void showAlert(String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
        javafx.scene.control.Alert.AlertType.WARNING);
    alert.setTitle("Warning");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void startGame(List<Player> players) {
    try {
      if (boardType.equals("treasure")) {
        TreasureGameBoardUI gameBoard = new TreasureGameBoardUI();
        Scene gameScene = gameBoard.createGameScene(boardType, primaryStage, players);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Star of Africa - " + boardType);
      } else {
        LadderGameBoardUI gameBoard = new LadderGameBoardUI();
        Scene gameScene = gameBoard.createGameScene(boardType, primaryStage, players);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Snakes and Ladders - " + boardType);
      }
    } catch (Exception e) {
      showAlert("Error starting game: " + e.getMessage());

    }
  }
}
