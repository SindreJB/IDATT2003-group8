package edu.ntnu.idi.idatt.model;

/**
 * Interface representing an action that can be performed when a player lands on
 * a tile.
 * This interface is designed to be extensible and JSON-serializable.
 */
public interface TileAction {
  /**
   * Performs the action on the specified player.
   *
   * @param player The player to perform the action on
   */
  void perform(Player player);

  /**
   * Gets a description of what this action does.
   *
   * @return A human-readable description of the action
   */
  String getDescription();

  /**
   * Gets the type of action. Used for JSON serialization.
   *
   * @return The type identifier for this action
   */
  String getType();
}