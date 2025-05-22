package edu.ntnu.idi.idatt.ui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Creates and manages the information panel UI components
 * Does not handle game logic or state
 */
public class InfoTable {
  private Label statusLabel;
  private Label gameInfoLabel;
  private Label moveCounterLabel; 
  private DiceUI diceView;

  /**
   * Creates a new InfoTable
   */
  public InfoTable() {
    statusLabel = new Label();
    gameInfoLabel = new Label();
    moveCounterLabel = new Label();
    moveCounterLabel.setVisible(false);
    diceView = new DiceUI();
  }

  /**
   * Creates the control panel containing game controls and information displays
   * 
   * @param rollAction Action to execute when roll button is clicked
   * @return VBox containing all control panel elements
   */
  public VBox createControlPanel(Runnable rollAction) {
    VBox panel = new VBox(15);
    panel.setStyle("-fx-padding: 20; -fx-alignment: top-center; -fx-spacing: 15;");

    // Style the move counter label
    moveCounterLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

    panel.getChildren().addAll(
        createStatusDisplay(),
        moveCounterLabel,
        diceView.createDicePanel(rollAction),
        createGameInfoArea());

    return panel;
  }

  /**
   * Creates the status display component
   * 
   * @return Label for showing player turn information
   */
  private Label createStatusDisplay() {
    statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    return statusLabel;
  }

  /**
   * Creates the game information area
   * 
   * @return Label for displaying game information
   */
  private Label createGameInfoArea() {
    gameInfoLabel.setWrapText(true);
    gameInfoLabel.setPrefWidth(200);
    gameInfoLabel.setPrefHeight(100);
    gameInfoLabel.setStyle(
        "-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 5; " +
        "-fx-background-color: #f8f8f8; -fx-font-size: 14px;");
    return gameInfoLabel;
  }

  /**
   * Gets the status label for updating text
   * 
   * @return The status label
   */
  public Label getStatusLabel() {
    return statusLabel;
  }

  /**
   * Gets the game info label for updating text
   * 
   * @return The game info label
   */
  public Label getGameInfoLabel() {
    return gameInfoLabel;
  }

  /**
   * Gets the dice view component
   * 
   * @return The DiceView
   */
  public DiceUI getDiceView() {
    return diceView;
  }

  /**
   * Updates the dice display with a specific value
   * 
   * @param value The dice value to display
   */
  public void updateDiceDisplay(int value) {
    diceView.displayDiceValue(value);
  }
  /**
   * Enables or disables the roll button
   * 
   * @param enabled True to enable, false to disable
   */
  public void setRollEnabled(boolean enabled) {
    diceView.setRollEnabled(enabled);
  }
  
  /**
   * Updates the move counter display
   * 
   * @param remaining Number of moves remaining
   */
  public void updateMoveCounter(int remaining) {
    if (remaining > 0) {
      moveCounterLabel.setText("Moves remaining: " + remaining);
      moveCounterLabel.setVisible(true);
    } else {
      moveCounterLabel.setVisible(false);
    }
  }

  public void updateCurrentPlayer(int playerIndex) {
    statusLabel.setText("Current Player: " + (playerIndex + 1));
  }
  public void updateDiceResult(int result) {
    diceView.displayDiceValue(result);
  }
  
  /**
   * Sets the action to be executed when the roll button is clicked
   * 
   * @param action The event handler for the roll button
   */
  public void setRollButtonAction(javafx.event.EventHandler<javafx.event.ActionEvent> action) {
    diceView.getRollButton().setOnAction(action);
  }
}