package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
  private final Dice dice;
  private final List<Tile> board = new ArrayList<>();

  public BoardGame() {
    this.dice = new Dice();
    initializeBoard();
  }

  private void initializeBoard() {
    board.add(new Tile(0));
    for(int i = 1; i <= 90 ; i++) {
      this.board.add(new Tile(i));
    }
    setLadderTiles();
    setSnakeTiles();
  }

  public void setLadderTiles() {
    board.get(8).setAction(new LadderAction(board.get(22)));
    board.get(27).setAction(new LadderAction(board.get(55)));
    board.get(40).setAction(new LadderAction(board.get(77)));
    board.get(60).setAction(new LadderAction(board.get(82)));
  }

  public void setSnakeTiles() {
    board.get(16).setAction(new SnakeAction(board.get(6)));
    board.get(49).setAction(new SnakeAction(board.get(11)));
    board.get(64).setAction(new SnakeAction(board.get(60)));
    board.get(73).setAction(new SnakeAction(board.get(67)));
    board.get(89).setAction(new SnakeAction(board.get(68)));
    board.get(89).setAction(new SnakeAction(board.get(78)));

  }

  public Tile movePlayer(Player player) {
    int toMove = dice.rollDice();
    System.out.println(toMove);
    setPlayerTile(player, toMove);
    return player.getCurrentTile();
  }

  public Tile setPlayerTile(Player player, int toMove) {
    if (player.getCurrentTileId()+toMove > 90) {
      player.setCurrentTile(board.get(2*90-(player.getCurrentTileId() + toMove)));
    } else {
    player.setCurrentTile(board.get(player.getCurrentTileId() + toMove));
    }
    if (player.getCurrentTile().hasAction()) {
      player.setCurrentTile(preformTileAction(player.getCurrentTile()));
    }
    return player.getCurrentTile();
  }

  public Tile preformTileAction(Tile tile) {
    System.out.println("action");
    return tile.getLandAction().tileActionResult();
  }


  public Tile getTile(int tileId) {
    return board.get(tileId);
  }
  

}
