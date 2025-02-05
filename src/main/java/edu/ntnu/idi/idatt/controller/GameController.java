package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.models.*;

public class GameController {

  private final BoardGame boardGame;
  private final Player player1;
  private final Player player2;
  private Player currentPlayer;

  public GameController() {
    boardGame = new BoardGame();
    boardGame.createBoard();
    boardGame.createDice();
    player1 = new Player("Player 1");
    player2 = new Player("Player 2");
    boardGame.addPlayer(player1);
    boardGame.addPlayer(player2);
    currentPlayer = player1;
  }

  public void play() {
    while (true) {
      int roll = boardGame.getDice().rollDice();
      System.out.println(currentPlayer.getName() + " rolled a " + roll);
      currentPlayer.move(roll);
      System.out.println(currentPlayer.getName() + " is now on tile " +
          currentPlayer.getCurrentTile());

      if (currentPlayer.getCurrentTile() >= 90) {
        System.out.println(currentPlayer.getName() + " wins!");
        break;
      }

      switchPlayer();
    }
  }

  private void switchPlayer() {
    currentPlayer = (currentPlayer == player1) ? player2 : player1;
  }
}