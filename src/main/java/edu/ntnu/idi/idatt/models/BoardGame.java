package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
  private final Board board;
  private Player currentPlayer;
  private final List<Player> players = new ArrayList<>();
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



  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    board.initializeBoard();
  }
  public void createDice() {
    dice = new Dice();
  }
}
