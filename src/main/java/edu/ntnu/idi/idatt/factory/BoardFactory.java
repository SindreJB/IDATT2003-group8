package edu.ntnu.idi.idatt.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.LadderAction;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.persistence.JsonHandler;

/**
 * Factory class for creating and managing board configurations.
 * Uses JSON for storing and loading board layouts and properties.
 */
public class BoardFactory {
  private static final String BOARDS_DIRECTORY = "boards";
  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Map<String, Board> boardCache = new HashMap<>();

  /**
   * Creates a new board from a JSON file.
   *
   * @param boardName The name of the board configuration to load
   * @return A configured board
   * @throws IOException If there is an error loading the board
   */
  public static Board createBoard(String boardName) throws IOException {
    // Check if the board is in cache
    if (boardCache.containsKey(boardName)) {
      return boardCache.get(boardName);
    }

    // Otherwise, load from file
    String filePath = BOARDS_DIRECTORY + File.separator + boardName + ".json";
    Board board = loadBoardFromFile(filePath);

    // Cache the board for future use
    boardCache.put(boardName, board);
    return board;
  }

  /**
   * Loads a board from a JSON file.
   *
   * @param filePath Path to the JSON file
   * @return The loaded board
   * @throws IOException If there is an error reading the file
   */
  private static Board loadBoardFromFile(String filePath) throws IOException {
    return JsonHandler.readFromJson(filePath, Board.class);
  }

  /**
   * Saves a board configuration to a JSON file.
   *
   * @param board     The board to save
   * @param boardName The name to save the board as
   * @throws IOException If there is an error writing the file
   */
  public static void saveBoard(Board board, String boardName) throws IOException {
    // Create boards directory if it doesn't exist
    Path dirPath = Paths.get(BOARDS_DIRECTORY);
    if (!Files.exists(dirPath)) {
      Files.createDirectories(dirPath);
    }

    String filePath = BOARDS_DIRECTORY + File.separator + boardName + ".json";
    JsonHandler.writeToJson(board, filePath);

    // Update cache
    boardCache.put(boardName, board);
  }

  /**
   * Gets a list of available board configurations.
   *
   * @return A list of board names
   * @throws IOException If there is an error reading the directory
   */
  public static List<String> getAvailableBoards() throws IOException {
    Path dirPath = Paths.get(BOARDS_DIRECTORY);

    // Create directory if it doesn't exist
    if (!Files.exists(dirPath)) {
      Files.createDirectories(dirPath);
      return List.of(); // Return empty list if directory was just created
    }

    // List JSON files in the directory
    return Files.list(dirPath)
        .filter(path -> path.toString().toLowerCase().endsWith(".json"))
        .map(path -> path.getFileName().toString())
        .map(filename -> filename.substring(0, filename.lastIndexOf('.')))
        .collect(Collectors.toList());
  }

  /**
   * Creates a classic Snakes and Ladders board configuration.
   *
   * @return A configured board
   */
  public static Board createClassicBoard() {
    Board board = new Board(9, 10);
    board.setName("Classic");
    board.setDescription("A classic Snakes and Ladders board with standard configuration");

    // Add ladders (going up)
    addLadder(board, 1, 38);
    addLadder(board, 4, 14);
    addLadder(board, 9, 31);
    addLadder(board, 21, 42);
    addLadder(board, 28, 84);
    addLadder(board, 36, 44);
    addLadder(board, 51, 67);

    // Add snakes (going down)
    addLadder(board, 16, 6); // Snake is just a ladder going down!
    addLadder(board, 47, 26);
    addLadder(board, 49, 11);
    addLadder(board, 56, 53);
    addLadder(board, 62, 19);
    addLadder(board, 64, 60);
    addLadder(board, 87, 24);

    return board;
  }

  /**
   * Creates an easier version of the Snakes and Ladders board.
   *
   * @return A configured board
   */
  public static Board createEasyBoard() {
    Board board = new Board(9, 10);
    board.setName("Easy");
    board.setDescription("An easier version with more ladders and fewer snakes");

    // Add ladders (going up)
    addLadder(board, 1, 38);
    addLadder(board, 4, 14);
    addLadder(board, 9, 31);
    addLadder(board, 21, 42);
    addLadder(board, 28, 84);
    addLadder(board, 36, 44);
    addLadder(board, 51, 67);
    addLadder(board, 30, 50);
    addLadder(board, 45, 70);

    // Add snakes (fewer than classic)
    addLadder(board, 47, 26);
    addLadder(board, 64, 60);
    addLadder(board, 87, 50);

    return board;
  }

  /**
   * Creates a difficult version of the Snakes and Ladders board.
   *
   * @return A configured board
   */
  public static Board createDifficultBoard() {
    Board board = new Board(9, 10);
    board.setName("Difficult");
    board.setDescription("A challenging board with more snakes and fewer ladders");

    // Add ladders (fewer than classic)
    addLadder(board, 4, 14);
    addLadder(board, 21, 42);
    addLadder(board, 51, 67);

    // Add snakes (more than classic)
    addLadder(board, 16, 6);
    addLadder(board, 47, 26);
    addLadder(board, 49, 11);
    addLadder(board, 56, 53);
    addLadder(board, 62, 19);
    addLadder(board, 64, 60);
    addLadder(board, 87, 24);
    addLadder(board, 30, 10);
    addLadder(board, 40, 20);
    addLadder(board, 70, 40);
    addLadder(board, 85, 65);

    return board;
  }

  /**
   * Helper method to add a ladder or snake to a board.
   *
   * @param board       The board to add the ladder to
   * @param startTileId The starting tile ID
   * @param endTileId   The ending tile ID
   */
  private static void addLadder(Board board, int startTileId, int endTileId) {
    Tile tile = board.getTile(startTileId);
    tile.setAction(new LadderAction(startTileId, endTileId));
  }

  /**
   * Generates and saves default boards if they don't exist.
   * Call this method to ensure standard boards are available.
   *
   * @throws IOException If there is an error saving the boards
   */
  public static void generateDefaultBoards() throws IOException {
    Path dirPath = Paths.get(BOARDS_DIRECTORY);
    if (!Files.exists(dirPath)) {
      Files.createDirectories(dirPath);
    }

    // Create and save classic board
    Board classicBoard = createClassicBoard();
    saveBoard(classicBoard, "classic");

    // Create and save easy board
    Board easyBoard = createEasyBoard();
    saveBoard(easyBoard, "easy");

    // Create and save difficult board
    Board difficultBoard = createDifficultBoard();
    saveBoard(difficultBoard, "difficult");
  }
}