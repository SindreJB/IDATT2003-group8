package edu.ntnu.idi.idatt.model;

/**
 * The TileAction interface represents an action that can be performed on a
 * tile.
 * Implementing classes should provide the logic for the action and return the
 * result
 * of the action as an integer.
 */
public interface TileAction {

  int tileActionResult();
}
