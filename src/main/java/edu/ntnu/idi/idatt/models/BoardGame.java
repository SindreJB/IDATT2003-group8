package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGame {
  private final Board board;
  private Player currentPlayer;
  private final List<Player> players = new ArrayList<>();
  private final Map<Integer, Tile> tileHasAction = new HashMap<Integer, Tile>();
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
}
