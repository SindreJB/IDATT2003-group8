package edu.ntnu.idi.idatt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.idi.idatt.factory.BoardFactory;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.persistence.CsvHandler;

/**
 * The GameController class manages the flow of the Snakes and Ladders game.
 */
public class GameController {

  private Board board;
  private static Player currentPlayer;
  private List<Player> players = new ArrayList<>();
  private static final String PLAYERS_FILE = "players.csv";

  /**
   * Constructs a new GameController and initializes the game board and players.
   */
  public GameController() {
    initializeBoard();
    loadPlayers();
  }

  /**
   * Initializes the game board, either from a saved configuration or a default
   * one.
   */
  private void initializeBoard() {
    try {
      // Try to initialize default boards if they don't exist
      BoardFactory.generateDefaultBoards();

      // Get available boards
      List<String> availableBoards = BoardFactory.getAvailableBoards();

      if (availableBoards.isEmpty()) {
        // If no boards are available, create a default one
        System.out.println("No board configurations found. Creating a classic board.");
        board = BoardFactory.createClassicBoard();
      } else {
        // If boards are available, let the user choose one
        System.out.println("Available board configurations:");
        for (int i = 0; i < availableBoards.size(); i++) {
          System.out.println((i + 1) + ". " + availableBoards.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a board (enter number): ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (selection > 0 && selection <= availableBoards.size()) {
          String boardName = availableBoards.get(selection - 1);
          board = BoardFactory.createBoard(boardName);
          System.out.println("Loaded board: " + board.getName());
          System.out.println(board.getDescription());
        } else {
          System.out.println("Invalid selection. Using classic board.");
          board = BoardFactory.createBoard("classic");
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading board configurations: " + e.getMessage());
      System.out.println("Using default board setup.");
      board = new Board(9, 10);
    }
  }

  /**
   * Loads players from a CSV file or prompts the user to add players.
   */
  private void loadPlayers() {
    try {
      // Try to load players from file
      List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv(PLAYERS_FILE);

      if (!loadedPlayers.isEmpty()) {
        System.out.println("Found saved players:");
        for (int i = 0; i < loadedPlayers.size(); i++) {
          System.out.println((i + 1) + ". " + loadedPlayers.get(i).getName());
        }

        System.out.print("Use these players? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("y")) {
          players = loadedPlayers;
          currentPlayer = players.get(0);
          return;
        }
      }

      // If no players were loaded or the user chose not to use them
      addPlayersManually();

    } catch (IOException e) {
      System.err.println("Error loading players: " + e.getMessage());
      addPlayersManually();
    }
  }

  /**
   * Prompts the user to add players manually.
   */
  private void addPlayersManually() {
    int maxPlayers = 4;
    List<Player> newPlayers = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);
    for (int i = 1; i <= maxPlayers; i++) {
      System.out.println("Enter the name of player " + i + ": ");
      System.out.println("If you don't want to add more players, press enter");
      String playerName = scanner.nextLine().trim();
      if (playerName.isEmpty()) {
        break;
      }
      Player player = new Player(playerName, 1);
      newPlayers.add(player);
    }

    if (newPlayers.isEmpty()) {
      System.out.println("No players added. Adding default players.");
      newPlayers.add(new Player("Player 1", 1));
      newPlayers.add(new Player("Player 2", 1));
    }

    players = newPlayers;
    currentPlayer = players.get(0);

    // Ask if the user wants to save the players
    System.out.print("Save these players for future games? (y/n): ");
    String choice = scanner.nextLine().trim().toLowerCase();
    if (choice.equals("y")) {
      try {
        CsvHandler.savePlayersToCsv(players, PLAYERS_FILE);
        System.out.println("Players saved successfully.");
      } catch (IOException e) {
        System.err.println("Error saving players: " + e.getMessage());
      }
    }
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
   * Executes the current player's turn and moves the player on the board.
   */
  private void playCurrentPlayer() {
    // Game logic for moving player and executing tile actions would go here
    // For now, this is a placeholder
    System.out.println(currentPlayer.getName() + " rolled the dice");

    // Simulate dice roll - would be replaced with actual dice roll in full
    // implementation
    int roll = (int) (Math.random() * 6) + 1;
    System.out.println("Rolled: " + roll);

    // Move player
    int currentPosition = currentPlayer.getTileId();
    int newPosition = Math.min(currentPosition + roll, board.getNumberOfTiles());
    currentPlayer.setTileId(newPosition);

    System.out.println(currentPlayer.getName() + " moved to tile " + newPosition);

    // Check if the tile has an action
    Tile currentTile = board.getTile(newPosition);
    if (currentTile.getAction() != null) {
      System.out.println("Action triggered: " + currentTile.getAction().getDescription());
      currentTile.performAction(currentPlayer);
      System.out.println(currentPlayer.getName() + " is now on tile " + currentPlayer.getTileId());
    }
  }

  /**
   * Checks if the current player has met the victory conditions.
   *
   * @return true if the current player has won, false otherwise
   */
  private Boolean checkVictoryConditions() {
    if (currentPlayer.getTileId() >= board.getNumberOfTiles()) {
      System.out.println(currentPlayer.getName() + " wins!");
      return true;
    }
    return false;
  }

  /**
   * Gets the game board.
   *
   * @return The current board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Gets the list of players.
   *
   * @return The list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Gets the current player.
   *
   * @return The current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}