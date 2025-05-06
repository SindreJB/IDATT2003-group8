package edu.ntnu.idi.idatt.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Creates and manages the information panel UI components
 * Does not handle game logic or state
 */
public class InfoTable {
  private Label statusLabel;
  private Label gameInfoLabel;
  private DiceUI diceView;

  /**
   * Creates a new InfoTable
   */
  public InfoTable() {
    statusLabel = new Label();
    gameInfoLabel = new Label();
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
    panel.setPadding(new Insets(20));
    panel.setAlignment(Pos.TOP_CENTER);

    panel.getChildren().addAll(
        createStatusDisplay(),
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
        "-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: #f8f8f8;");
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
}