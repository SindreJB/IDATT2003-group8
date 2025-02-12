package edu.ntnu.idi.idatt.controller;

import java.util.List;
import java.util.Scanner;

import edu.ntnu.idi.idatt.models.BoardGame;
import edu.ntnu.idi.idatt.models.LadderAction;
import edu.ntnu.idi.idatt.models.Player;
import edu.ntnu.idi.idatt.models.Tile;

public class GameController {

  private final BoardGame boardGame;
  private static Player currentPlayer;

  public GameController() {
    boardGame = new BoardGame();
    boardGame.createBoard();
    boardGame.createDice();
  }

  private void switchPlayer() {
    List<Player> players = boardGame.getPlayers();
    int currentIndex = players.indexOf(currentPlayer);
    currentPlayer = players.get((currentIndex + 1) % players.size());
  }

  public void playLadderGame() {
    while (!checkVictoryConditions()) {
      displayCurrentPlayerInfo();
      playCurrentPlayer();
      performTileAction();
      switchPlayer();
      System.out.println(" ");
    }
  }

  private static void displayCurrentPlayerInfo() {
    System.out.println("It's " + currentPlayer.getName() + "'s turn");
    System.out.println(currentPlayer.getName() + " is on tile " + currentPlayer.getCurrentTile());
    System.out.println("Press enter to roll the dice");
    Scanner sc = new Scanner(System.in);
    sc.nextLine();
  }

  private void AddPlayers() {
    int maxPlayers = 4;
    Scanner sc = new Scanner(System.in);
    for (int i = 1; i <= maxPlayers; i++) {
      System.out.println("Enter the name of player " + i + ": ");
      System.out.println("If you don't want to add more players, press enter");
      String playerName = sc.nextLine();
      if (playerName.isEmpty()) {
        break;
      }
      Player player = new Player(playerName);
      boardGame.addPlayer(player);
      if (currentPlayer == null) {
        currentPlayer = player;
      }
    }
  }

  public void initializeLadderGame() {
    AddPlayers();
    boardGame.createBoard();
    boardGame.createDice();
    boardGame.setLadderTiles();
    boardGame.setSnakeTiles();
  }

  private void playCurrentPlayer() {
    int roll = boardGame.getDice().rollDice();
    System.out.println(currentPlayer.getName() + " rolled a " + roll);
    currentPlayer.move(roll);
    System.out.println(currentPlayer.getName() + " is now on tile " + currentPlayer.getCurrentTile());
    movePlayerToTile(currentPlayer);
  }

  private void movePlayerToTile(Player player) {
    Tile currentTile = boardGame.getTile(player.getCurrentTile());
    if (currentTile != null && currentTile.getLandAction() != null) {
      int oldPosition = player.getCurrentTile();
      currentTile.landPlayer(currentPlayer);
      System.out.println(player.getName() + " encountered a " +
          (currentTile.getLandAction() instanceof LadderAction ? "ladder" : "snake") +
          " and moved from " + oldPosition + " to " + player.getCurrentTile());
    }
  }

  private Boolean checkVictoryConditions() {
    if (currentPlayer.getCurrentTile() >= 90) {
      System.out.println(currentPlayer.getName() + " wins!");
      return true;
    }
    return false;
  }

  private void performTileAction() {
    Tile currentTile = boardGame.getTile(currentPlayer.getCurrentTile());
    if (currentTile != null && currentTile.getLandAction() != null) {
      currentTile.landPlayer(currentPlayer);
    }
  }

  private void printActionTiles() {
    System.out.println("Tiles with actions: ");
    boardGame.getTilesWithAction().forEach((key, value) -> System.out.println(key));
  }
}