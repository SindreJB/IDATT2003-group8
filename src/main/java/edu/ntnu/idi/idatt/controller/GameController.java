package edu.ntnu.idi.idatt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.idi.idatt.models.BoardGame;
import edu.ntnu.idi.idatt.models.Player;

public class GameController {

  private final BoardGame boardGame;
  private static Player currentPlayer;
  private List<Player> players = new ArrayList<>();

  public GameController() {
    boardGame = new BoardGame();
    AddPlayers();
  }
  

  private void switchPlayer() {
    int currentIndex = players.indexOf(currentPlayer);
    currentPlayer = players.get((currentIndex + 1) % players.size());
  }

  public void playLadderGame() {
    while (!checkVictoryConditions()) {
      playCurrentPlayer();
      displayCurrentPlayerInfo();
      switchPlayer();
      System.out.println(" ");
    }
  }

  private static void displayCurrentPlayerInfo() {
    System.out.println("It's " + currentPlayer.getName() + "'s turn");
    System.out.println(currentPlayer.getName() + " is on tile " + currentPlayer.getCurrentTileId());
    System.out.println("Press enter to roll the dice");
    Scanner sc = new Scanner(System.in);
    sc.nextLine();
  }

  private void AddPlayers() {
    int maxPlayers = 4;
    List<Player> createPlayers = new ArrayList<>();

    Scanner sc = new Scanner(System.in);
    for (int i = 1; i <= maxPlayers; i++) {
      System.out.println("Enter the name of player " + i + ": ");
      System.out.println("If you don't want to add more players, press enter");
      String playerName = sc.nextLine();
      if (playerName.isEmpty()) {
        break;
      }
      Player player = new Player(playerName, boardGame.getTile(1));
      createPlayers.add(player);
      if (currentPlayer == null) {
        currentPlayer = player;
      }
    }
    this.players = createPlayers;
  }

  private void playCurrentPlayer() {
    System.out.println(currentPlayer.getName() + " has landed on " + boardGame.movePlayer(currentPlayer).getTileId());

  }


  private Boolean checkVictoryConditions() {
    if (currentPlayer.getCurrentTileId() == 90) {
      System.out.println(currentPlayer.getName() + " wins!");
      return true;
    }
    return false;
  }
}