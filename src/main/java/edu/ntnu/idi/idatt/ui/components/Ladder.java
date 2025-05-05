package edu.ntnu.idi.idatt.ui.components;

import javafx.scene.shape.Polygon;

public class Ladder extends Polygon {
  private double length = 100.0;

  public Ladder(int length) {
    super(
        0.0, length / 6.0 / 2, // Left side of shaft
        length - length / 3.0, length / 6.0 / 2, // Right side of shaft top
        length - length / 3.0, length / 3.0 / 2, // Start of arrow head (top)
        length, 0.0, // Arrow tip
        length - length / 3.0, -length / 3.0 / 2, // Start of arrow head (bottom)
        length - length / 3.0, -length / 6.0 / 2, // Right side of shaft bottom
        0.0, -length / 6.0 / 2 // Left side of shaft
    );
    this.length = length;
  }
}
