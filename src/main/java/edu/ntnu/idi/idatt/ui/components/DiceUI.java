package edu.ntnu.idi.idatt.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A component that displays dice and a roll button
 */
public class DiceUI {
  private HBox diceContainer;
  private Button rollButton;
  private final String rollButtonStyle = "-fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand; " +
                                        "-fx-background-color: #1976d2; -fx-text-fill: white;";

  /**
   * Creates a new DiceView
   */
  public DiceUI() {
    diceContainer = new HBox(10);
    rollButton = new Button("Roll Dice");

    // Configure the dice container
    diceContainer.setAlignment(Pos.CENTER);
    diceContainer.setPrefHeight(60);
    diceContainer.setPrefWidth(100);
    diceContainer.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f8f8f8;");
    
    // Style the roll button
    rollButton.setStyle(rollButtonStyle);
    
    // Add hover effect to roll button
    rollButton.setOnMouseEntered(e -> rollButton.setStyle(rollButtonStyle.replace("#1976d2", "#1565c0")));
    rollButton.setOnMouseExited(e -> rollButton.setStyle(rollButtonStyle));
  }

  /**
   * Creates the dice panel containing a roll button and dice display
   * 
   * @param rollAction Action to execute when roll button is clicked
   * @return VBox containing dice controls and display
   */
  public VBox createDicePanel(Runnable rollAction) {
    VBox dicePanel = new VBox(10);
    dicePanel.setStyle("-fx-alignment: center; -fx-spacing: 10;");

    // Set up roll button action
    rollButton.setOnAction(e -> {
      if (rollAction != null) {
        rollAction.run();
      }
    });

    dicePanel.getChildren().addAll(rollButton, diceContainer);
    return dicePanel;
  }

  /**
   * Displays a dice value in the dice container
   * 
   * @param value The value to display
   */
  public void displayDiceValue(int value) {
    diceContainer.getChildren().clear();
    Label diceLabel = new Label(Integer.toString(value));
    diceLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    diceContainer.getChildren().add(diceLabel);
  }

  /**
   * Gets the dice container
   * 
   * @return The HBox container for dice
   */
  public HBox getDiceContainer() {
    return diceContainer;
  }

  /**
   * Enables or disables the roll button
   * 
   * @param enabled True to enable, false to disable
   */
  public void setRollEnabled(boolean enabled) {
    if (!enabled) {
      rollButton.setStyle(rollButtonStyle + "-fx-opacity: 0.7;");
    } else {
      rollButton.setStyle(rollButtonStyle);
    }
    rollButton.setDisable(!enabled);
  }
}