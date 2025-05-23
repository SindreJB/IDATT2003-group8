package edu.ntnu.idi.idatt.model;

/**
 * The Player class represents a player in the game with a name, piece type, and
 * a tile ID.
 */
public class Player {
  private final String name;
  private final String pieceType; // Type of game piece (e.g., "TopHat", "RaceCar")
  private int tileId;

  /**
   * Represents a player in the game.
   * Each player has a name, piece type, and a tile ID indicating their position
   * on the game board.
   *
   * @param name      the name of the player
   * @param pieceType the type of game piece used by the player
   * @param tileId    the ID of the tile where the player is currently located
   */
  public Player(String name, String pieceType, int tileId) {
    this.name = name;
    this.pieceType = pieceType;
    this.tileId = tileId;
  }

  /**
   * Retrieves the name of the player.
   *
   * @return the name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the piece type of the player.
   *
   * @return the piece type of the player.
   */
  public String getPieceType() {
    return pieceType;
  }

  /**
   * Retrieves the ID of the tile associated with the player.
   *
   * @return the ID of the tile as an Integer.
   */
  public Integer getTileId() {
    return this.tileId;
  }

  /**
   * Sets the tile ID for the player.
   *
   * @param tileId the ID of the tile to be set
   */
  public void setTileId(int tileId) {
    this.tileId = tileId;
  }
}