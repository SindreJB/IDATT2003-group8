package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGame {
  private final Board board;
  private Player currentPlayer;
  private final List<Player> players = new ArrayList<>();
  private final Map<Integer, Tile> tileHasAction = new HashMap<>();
  private Dice dice;

  public BoardGame() {
    this.board = new Board();
    this.dice = new Dice();
  }

  public Dice getDice() {
    return dice;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player player) {
    currentPlayer = player;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    board.initializeBoard();
  }
  public void createDice() {
    dice = new Dice();
  }

  public Map<Integer, Tile> getTilesWithAction() {
    return tileHasAction;
  }

  public void addTileAction(int tileId, TileAction action) {
    tileHasAction.put(tileId, new Tile(tileId, action));
  }

  public Tile getTile(int tileId) {
    return tileHasAction.get(tileId);
  }
  
  public void setLadderTiles() {
    addTileAction(8, new LadderAction(22));
    addTileAction(27, new LadderAction(55));
    addTileAction(40, new LadderAction(77));
    addTileAction(60, new LadderAction(82));
  }

  public void setSnakeTiles() {
    addTileAction(16, new SnakeAction(6));
    addTileAction(49, new SnakeAction(11));
    addTileAction(64, new SnakeAction(60));
    addTileAction(73, new SnakeAction(67));
    addTileAction(89, new SnakeAction(68));
    addTileAction(98, new SnakeAction(78));
  }
}
