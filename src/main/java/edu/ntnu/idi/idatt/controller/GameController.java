package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.models.*;
import java.util.List;
import java.util.Scanner;

public class GameController {

  private final BoardGame boardGame;
  private static Player currentPlayer;
  private boolean finished;

  public GameController() {
    boardGame = new BoardGame();
    boardGame.createBoard();
    boardGame.createDice();
    finished = false;
  }

  public void twoPlayerScript() {
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
    List<Player> players = boardGame.getPlayers();
    int currentIndex = players.indexOf(currentPlayer);
    currentPlayer = players.get((currentIndex + 1) % players.size());
  }

  public void playLadderGame() {
    Addplayers();
    initializeLadderGame();
    while (!finished) {
      playCurrentPlayer();
      performTileAction();
      switchPlayer();
    }
  }

  private void Addplayers() {
    Scanner sc = new Scanner(System.in);
    for (int i = 1; i <= 4; i++) {
      System.out.println("Enter the name of player " + i + ": ");
      System.out.println("If you don't want to add more players, press enter");
      String playerName = sc.nextLine();
      if (playerName.equals("")) {
        break;
      }
      Player player = new Player(playerName);
      boardGame.addPlayer(player);
      if (currentPlayer == null) {
        currentPlayer = player;
      }
    }
  }

  private void initializeLadderGame() {
    boardGame.createBoard();
    boardGame.createDice();
  }

  private void playCurrentPlayer() {
    int roll = boardGame.getDice().rollDice();
    System.out.println(currentPlayer.getName() + " rolled a " + roll);
    currentPlayer.move(roll);
    System.out.println(currentPlayer.getName() + " is now on tile " + currentPlayer.getCurrentTile());
    if (currentPlayer.getCurrentTile() >= 90) {
      System.out.println(currentPlayer.getName() + " wins!");
      finished = true;
    }
  }

  private void performTileAction() {
    //This code should check if the current tile has an action and perform it
    if(boardGame.getTilesWithAction().containsKey(currentPlayer.getCurrentTile())) {
      boardGame.getTilesWithAction().get(currentPlayer.getCurrentTile()).landPlayer(currentPlayer);
    }
  }

}