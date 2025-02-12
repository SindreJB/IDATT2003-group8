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
    getBoardTile(8).setAction(new LadderAction(getBoardTile(22)));
    getBoardTile(27).setAction(new LadderAction(getBoardTile(55)));
    getBoardTile(40).setAction(new LadderAction(getBoardTile(77)));
    getBoardTile(60).setAction(new LadderAction(getBoardTile(82)));
  }

  public void setSnakeTiles() {
    getBoardTile(16).setAction(new SnakeAction(getBoardTile(6)));
    getBoardTile(49).setAction(new SnakeAction(getBoardTile(11)));
    getBoardTile(64).setAction(new SnakeAction(getBoardTile(60)));
    getBoardTile(73).setAction(new SnakeAction(getBoardTile(67)));
    getBoardTile(89).setAction(new SnakeAction(getBoardTile(68)));
    getBoardTile(89).setAction(new SnakeAction(getBoardTile(78)));

  }

  public int movePlayer(Player player) {
    int playerTile = player.getTileId();
    int toMove = dice.rollDice();
    System.out.println(toMove);
    player.setTileId((setPlayerTile(playerTile, toMove)));
    return player.getTileId();
  }

  public int setPlayerTile(int playerTile, int toMove) {
    int tileOut;
    if (playerTile+toMove > 90) {
      tileOut =  (2*90-(playerTile  + toMove));
    } else {
    tileOut =  playerTile + toMove;
    }
    if (getBoardTile(playerTile).hasAction()) {
      tileOut =  preformTileAction(playerTile);
    }
    return tileOut;
  }

  public int preformTileAction(int tileId) {
    System.out.println("action");
    return getBoardTile(tileId).getLandAction().tileActionResult().getTileId();
  }


  public Tile getBoardTile(int tileId) {
    return board.get(tileId);
  }
  
}
