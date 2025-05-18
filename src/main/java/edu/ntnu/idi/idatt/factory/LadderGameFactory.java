package edu.ntnu.idi.idatt.factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

import edu.ntnu.idi.idatt.controller.BoardManager;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.BoardConfig;
import edu.ntnu.idi.idatt.model.LadderGameTile;

/**
 * Factory for creating ladder game boards from configurations
 */
public class LadderGameFactory {

  /**
   * Creates a board from a named configuration, creating the config file if it
   * doesn't exist
   *
   * @param boardName the name of the board to create ("standard", "empty", etc.)
   * @return the created board
   * @throws IOException if there's an error reading or writing files
   */
  public static Board createBoard(String boardName) throws IOException {
    // Check if the board configuration file exists
    Path boardPath = BoardManager.getBoardsDirectory().resolve(boardName + ".json");

    if (!Files.exists(boardPath)) {
      // Create the requested board configuration
      switch (boardName) {
        case "standard":
          BoardManager.createStandardBoard();
          break;
        case "empty":
          BoardManager.createEmptyBoard();
          break;
        case "custom":
          BoardManager.createCustomBoard(boardName);
          break;
        default:
          throw new IllegalArgumentException("Unknown board type: " + boardName);
      }
    }

    // Now load and create the board from the configuration
    BoardConfig config = BoardManager.loadBoard(boardName);
    return createBoardFromConfig(config);
  }

  /**
   * Creates a board from a BoardConfig object
   *
   * @param config the board configuration
   * @return the created board
   */
  public static Board createBoardFromConfig(BoardConfig config) {
    // Create a new board with specified dimensions
    Board board = new Board(config.getRows(), config.getColumns());
    board.setName(config.getName());
    board.setDescription(config.getDescription());

    // Add snakes
    for (int i = 0; i < config.getSnakeHeads().size(); i++) {
      int head = config.getSnakeHeads().get(i);
      int tail = config.getSnakeTails().get(i);
      addSnake(board, head, tail);
    }

    // Add ladders
    for (int i = 0; i < config.getLadderStarts().size(); i++) {
      int start = config.getLadderStarts().get(i);
      int end = config.getLadderEnds().get(i);
      addLadder(board, start, end);
    }

    // Add wormholes
    if (config.getWormholeStarts() != null) {
      for (int i = 0; i < config.getWormholeStarts().size(); i++) {
        int start = config.getWormholeStarts().get(i);
        addWormhole(board, start);
      }
    }

    return board;
  }

  /**
   * Adds a wormhole to the board
   *
   * @param board the board to add the wormhole to
   * @param start the wormhole's start position
   */
  private static void addWormhole(Board board, int start) {
    LadderGameTile startTile = board.getTile(start);
    // Generate a random destination for the wormhole
    Random random = new Random();
    int destination;
    do {
      destination = random.nextInt(board.getRows() * board.getColumns()) + 1;
    } while (destination == start);

    LadderGameTile endTile = board.getTile(destination);
    startTile.setWormhole(endTile);
  }

  /**
   * Adds a snake to the board
   *
   * @param board the board to add the snake to
   * @param head  the snake's head position
   * @param tail  the snake's tail position
   */
  private static void addSnake(Board board, int head, int tail) {
    LadderGameTile headTile = board.getTile(head);
    LadderGameTile tailTile = board.getTile(tail);
    headTile.setSnake(tailTile);
  }

  /**
   * Adds a ladder to the board
   *
   * @param board the board to add the ladder to
   * @param start the ladder's start position
   * @param end   the ladder's end position
   */
  private static void addLadder(Board board, int start, int end) {
    LadderGameTile startTile = board.getTile(start);
    LadderGameTile endTile = board.getTile(end);
    startTile.setLadder(endTile);
  }

  /**
   * Tries to load a board by name, falling back to creating a standard board if
   * it fails
   *
   * @param boardName the name of the board configuration file
   * @return the created board, or empty if creation fails
   */
  public static Optional<Board> tryCreateBoard(String boardName) {
    try {
      return Optional.of(createBoard(boardName));
    } catch (IOException | IllegalArgumentException e) {
      System.err.println("Could not load board '" + boardName + "': " + e.getMessage());
      try {
        return Optional.of(createBoard("standard"));
      } catch (IOException ex) {
        System.err.println("Could not create standard board: " + ex.getMessage());
        return Optional.empty();
      }
    }
  }
}