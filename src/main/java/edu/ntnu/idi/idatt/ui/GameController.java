package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.persistence.CsvHandler;

/**
 * The GameController class manages the flow of the Snakes and Ladders game.
 */
public class GameController {

  private final Board boardGame;
  private static Player currentPlayer;
  private List<Player> players = new ArrayList<>();

  /**
   * Constructs a new GameController and initializes the game board and players.
   */
  public GameController() {
    boardGame = new Board(9, 10);
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
   * Each player can select a piece type.
   */
  private void AddPlayers() {
    int maxPlayers = 4;
    List<Player> createPlayers = new ArrayList<>();

    String[] pieceTypes = { "TopHat", "RaceCar", "Cat", "Thimble", "Dog" };

    Scanner sc = new Scanner(System.in);
    for (int i = 1; i <= maxPlayers; i++) {
      System.out.println("Enter the name of player " + i + ": ");
      System.out.println("(If you don't want to add more players, press enter)");
      String playerName = sc.nextLine();

      if (playerName.isEmpty()) {
        break;
      }

      System.out.println("Select a piece type for " + playerName + ":");
      for (int j = 0; j < pieceTypes.length; j++) {
        System.out.println((j + 1) + ". " + pieceTypes[j]);
      }

      int selection = 0;
      boolean validSelection = false;

      while (!validSelection) {
        try {
          System.out.print("Enter selection (1-" + pieceTypes.length + "): ");
          String input = sc.nextLine();
          selection = Integer.parseInt(input);

          if (selection >= 1 && selection <= pieceTypes.length) {
            validSelection = true;
          } else {
            System.out.println("Invalid selection. Please try again.");
          }
        } catch (NumberFormatException e) {
          System.out.println("Please enter a valid number.");
        }
      }

      String pieceType = pieceTypes[selection - 1];
      Player player = new Player(playerName, pieceType, 1);
      createPlayers.add(player);

      if (currentPlayer == null) {
        currentPlayer = player;
      }

      System.out.println(playerName + " will use the " + pieceType + " piece.");
      System.out.println();
    }

    this.players = createPlayers;
  }

  /**
   * Saves the current players to a CSV file.
   * 
   * @param filePath the path to save the CSV file
   * @return true if the operation was successful, false otherwise
   */
  public boolean savePlayersToFile(String filePath) {
    try {
      CsvHandler.savePlayersToCsv(players, filePath);
      System.out.println("Players saved to " + filePath);
      return true;
    } catch (IOException e) {
      System.err.println("Error saving players: " + e.getMessage());
      return false;
    }
  }

  /**
   * Loads players from a CSV file.
   * 
   * @param filePath the path to the CSV file
   * @return true if the operation was successful, false otherwise
   */
  public boolean loadPlayersFromFile(String filePath) {
    try {
      List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv(filePath);

      if (loadedPlayers.isEmpty()) {
        System.out.println("No players found in file.");
        return false;
      }

      this.players = loadedPlayers;
      currentPlayer = players.get(0);

      System.out.println("Loaded " + players.size() + " players from " + filePath);
      return true;
    } catch (IOException e) {
      System.err.println("Error loading players: " + e.getMessage());
      return false;
    }
  }

  /**
   * Executes the current player's turn and moves the player on the board.
   */
  private void playCurrentPlayer() {
    // System.out.println(currentPlayer.getName() + " has landed on " +
    // boardGame.movePlayer(currentPlayer));
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