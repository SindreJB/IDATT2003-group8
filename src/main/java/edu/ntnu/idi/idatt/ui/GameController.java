package edu.ntnu.idi.idatt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;

/**
 * The GameController class manages the flow of the Snakes and Ladders game.
 */
public class GameController {

  private final BoardGame boardGame;
  private static Player currentPlayer;
  private List<Player> players = new ArrayList<>();

  /**
   * Constructs a new GameController and initializes the game board and players.
   */
  public GameController() {
    boardGame = new BoardGame();
    AddPlayers();
  }

  /**
   * Switches the current player to the next player in the list.
   */
  private void switchPlayer() {
    int currentIndex = players.indexOf(currentPlayer);
    currentPlayer = players.get((currentIndex + 1) % players.size());
  }

  /**
   * Starts the game loop for Snakes and Ladders.
   * The game continues until a player meets the victory conditions.
   */
  public void playLadderGame() {
    while (!checkVictoryConditions()) {
      playCurrentPlayer();
      displayCurrentPlayerInfo();
      switchPlayer();
      System.out.println(" ");
    }
  }

  /**
   * Displays the current player's information and waits for the player to roll
   * the dice.
   */
  private static void displayCurrentPlayerInfo() {
    System.out.println("It's " + currentPlayer.getName() + "'s turn");
    System.out.println(currentPlayer.getName() + " is on tile " + currentPlayer.getTileId());
    System.out.println("Press enter to roll the dice");
    Scanner sc = new Scanner(System.in);
    sc.nextLine();
  }

  /**
   * Prompts the user to add players to the game.
   * A maximum of 4 players can be added.
   */
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
      Player player = new Player(playerName, 1);
      createPlayers.add(player);
      if (currentPlayer == null) {
        currentPlayer = player;
      }
    }
    this.players = createPlayers;
  }

  /**
   * Executes the current player's turn and moves the player on the board.
   */
  private void playCurrentPlayer() {
    System.out.println(currentPlayer.getName() + " has landed on " + boardGame.movePlayer(currentPlayer));
  }

  /**
   * Checks if the current player has met the victory conditions.
   *
   * @return true if the current player has won, false otherwise
   */
  private Boolean checkVictoryConditions() {
    if (currentPlayer.getTileId() == 90) {
      System.out.println(currentPlayer.getName() + " wins!");
      return true;
    }
    return false;
  }
}