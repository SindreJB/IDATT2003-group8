package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The BoardGame class represents a board game with a dice and a board
 * consisting of tiles.
 * It initializes the board, sets ladder and snake tiles, and handles player
 * movements.
 */
public class BoardGame {
  private final Dice dice;
  private final List<Tile> board = new ArrayList<>();

  /**
   * Constructs a new BoardGame instance.
   * Initializes the dice and sets up the game board.
   */
  public BoardGame() {
    this.dice = new Dice();
    initializeBoard();
  }

  /**
   * Initializes the game board by adding tiles to it.
   * The board starts with a tile numbered 0, followed by tiles numbered 1 to 90.
   * After adding the tiles, it sets up the ladder and snake tiles on the board.
   */
  private void initializeBoard() {
    board.add(new Tile(0));
    for (int i = 1; i <= 90; i++) {
      this.board.add(new Tile(i));
    }
    setLadderTiles();
    setSnakeTiles();
  }

  /**
   * Sets the ladder tiles on the board. This method assigns ladder actions to
   * specific
   * board tiles, allowing players to move up to a higher tile when they land on
   * these tiles.
   * The following ladder actions are set:
   * <ul>
   * <li>Tile 8: Moves to tile 22</li>
   * <li>Tile 27: Moves to tile 55</li>
   * <li>Tile 40: Moves to tile 77</li>
   * <li>Tile 60: Moves to tile 82</li>
   * </ul>
   */
  public void setLadderTiles() {
    getBoardTile(8).setAction(new LadderAction(22));
    getBoardTile(27).setAction(new LadderAction(55));
    getBoardTile(40).setAction(new LadderAction(77));
    getBoardTile(60).setAction(new LadderAction(82));
  }

  /**
   * Sets the snake tiles on the board. Each snake tile is associated with a
   * specific board tile and a corresponding SnakeAction that defines the
   * destination tile when a player lands on the snake tile.
   * 
   * The following snake tiles are set:
   * - Tile 16 moves to Tile 6
   * - Tile 49 moves to Tile 11
   * - Tile 64 moves to Tile 60
   * - Tile 73 moves to Tile 67
   * - Tile 89 moves to Tile 68
   * - Tile 89 moves to Tile 78 (overwrites the previous action for Tile 89)
   */
  public void setSnakeTiles() {
    getBoardTile(16).setAction(new SnakeAction(6));
    getBoardTile(49).setAction(new SnakeAction(11));
    getBoardTile(64).setAction(new SnakeAction(60));
    getBoardTile(73).setAction(new SnakeAction(67));
    getBoardTile(89).setAction(new SnakeAction(68));
    getBoardTile(89).setAction(new SnakeAction(78));

  }

  /**
   * Moves the player on the board based on the result of a dice roll.
   *
   * @param player the player to be moved
   * @return the new tile ID of the player after moving
   */
  public int movePlayer(Player player) {
    int playerTile = player.getTileId();
    int toMove = dice.rollDice();
    System.out.println(toMove);
    player.setTileId((setPlayerTile(playerTile, toMove)));
    return player.getTileId();
  }

  /**
   * Sets the player's tile to a new position based on the current tile and the
   * number of tiles to move.
   * If the new position exceeds 90, it wraps around in the opposite direction.
   * If the current tile has an action, it performs the action and updates the
   * tile position accordingly.
   *
   * @param playerTile the current tile position of the player
   * @param toMove     the number of tiles to move
   * @return the new tile position after moving and performing any tile actions
   */
  public int setPlayerTile(int playerTile, int toMove) {
    int tileOut;
    if (playerTile + toMove > 90) {
      tileOut = (2 * 90 - (playerTile + toMove));
    } else {
      tileOut = playerTile + toMove;
    }
    if (getBoardTile(playerTile).hasAction()) {
      tileOut = preformTileAction(playerTile);
    }
    return tileOut;
  }

  /**
   * Performs an action on the specified tile.
   *
   * @param tileId the ID of the tile on which to perform the action
   * @return the result of the tile action
   */
  public int preformTileAction(int tileId) {
    System.out.println("action");
    return getBoardTile(tileId).getLandAction().tileActionResult();
  }

  /**
   * Retrieves the Tile object from the board based on the given tile ID.
   *
   * @param tileId the ID of the tile to retrieve
   * @return the Tile object corresponding to the specified tile ID
   */
  public Tile getBoardTile(int tileId) {
    return board.get(tileId);
  }

}
