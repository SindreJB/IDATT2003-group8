package edu.ntnu.idi.idatt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.ntnu.idi.idatt.model.BoardConfig;
import edu.ntnu.idi.idatt.persistence.JsonHandler;

public class BoardManager {

  public static Path getBoardsDirectory() {
    Path dataDir = Paths.get("./", "data", "boards");
    try {
      Files.createDirectories(dataDir);
    } catch (IOException e) {
      System.err.println("Failed to create boards directory: " + e.getMessage());
    }
    return dataDir;
  }

  public static void createEmptyBoard() {
    BoardConfig emptyBoard = new BoardConfig(
        "Empty Board",
        "An empty 9x10 Snakes and Ladders board",
        10, 9);

    try {
      Path boardPath = getBoardsDirectory().resolve("empty.json");
      JsonHandler.writeToJson(emptyBoard, boardPath.toString());
      System.out.println("Empty board saved to: " + boardPath);
    } catch (IOException e) {
      System.err.println("Error saving empty board: " + e.getMessage());
    }
  }

  public static void createStandardBoard() {
    BoardConfig standardBoard = new BoardConfig(
        "Standard Board",
        "Classic 9x10 Snakes and Ladders board with traditional placement",
        10, 9);
    // Add ladders (from bottom to top)
    standardBoard.addLadder(3, 22);
    standardBoard.addLadder(11, 26);
    standardBoard.addLadder(21, 42);
    standardBoard.addLadder(36, 57);
    standardBoard.addLadder(51, 67);
    standardBoard.addLadder(71, 83);

    // Add snakes (from head to tail)
    standardBoard.addSnake(17, 7);
    standardBoard.addSnake(29, 15);
    standardBoard.addSnake(47, 26);
    standardBoard.addSnake(62, 43);
    standardBoard.addSnake(87, 61);
    standardBoard.addSnake(95, 73);

    // Add wormholes (entrance)
    standardBoard.addWormhole(5);
    standardBoard.addWormhole(13);
    standardBoard.addWormhole(45);

    try {
      Path boardPath = getBoardsDirectory().resolve("standard.json");
      JsonHandler.writeToJson(standardBoard, boardPath.toString());
      System.out.println("Standard board saved to: " + boardPath);
    } catch (IOException e) {
      System.err.println("Error saving standard board: " + e.getMessage());
    }
  }

  /**
   * Creates a hardcoded custom board and saves it to a JSON file
   *
   * @param boardName the name for the saved board
   * @return The path to the created JSON file
   * @throws IOException if there's an error writing the file
   */
  public static Path createCustomBoard(String boardName) throws IOException {
    BoardConfig customBoard = new BoardConfig(
        "Custom Board",
        "A 10x10 board with custom snake and ladder placement",
        10, 10);

    // Add custom ladders (start -> end)
    customBoard.addLadder(4, 23);
    customBoard.addLadder(14, 35);
    customBoard.addLadder(28, 49);
    customBoard.addLadder(37, 65);
    customBoard.addLadder(48, 68);
    customBoard.addLadder(57, 82);
    customBoard.addLadder(79, 96);

    // Add custom snakes (head -> tail)
    customBoard.addSnake(24, 6);
    customBoard.addSnake(39, 18);
    customBoard.addSnake(52, 31);
    customBoard.addSnake(63, 42);
    customBoard.addSnake(75, 54);
    customBoard.addSnake(88, 67);
    customBoard.addSnake(97, 78);

    // add wormholes (entrance)
    customBoard.addWormhole(10);
    customBoard.addWormhole(20);
    customBoard.addWormhole(30);
    customBoard.addWormhole(40);

    Path boardPath = getBoardsDirectory().resolve(boardName + ".json");
    JsonHandler.writeToJson(customBoard, boardPath.toString());
    System.out.println("Custom board saved to: " + boardPath);

    return boardPath;
  }

  /**
   * Creates a board with wormholes and saves it to a JSON file
   *
   * @return The path to the created JSON file
   * @throws IOException if there's an error writing the file
   */
  public static Path createWormholeBoard() throws IOException {
    BoardConfig wormholeBoard = new BoardConfig(
        "Wormhole Board",
        "A 10x10 board with wormholes, snakes, and ladders",
        10, 10);

    // Add ladders (start -> end)
    wormholeBoard.addLadder(4, 25);
    wormholeBoard.addLadder(13, 46);
    wormholeBoard.addLadder(50, 69);

    // Add snakes (head -> tail)
    wormholeBoard.addSnake(54, 31);
    wormholeBoard.addSnake(89, 53);
    wormholeBoard.addSnake(99, 41);

    // Add wormholes (entrance -> exit)
    wormholeBoard.addWormhole(22);
    wormholeBoard.addWormhole(77);
    wormholeBoard.addWormhole(63);

    Path boardPath = getBoardsDirectory().resolve("wormhole.json");
    JsonHandler.writeToJson(wormholeBoard, boardPath.toString());
    System.out.println("Wormhole board saved to: " + boardPath);

    return boardPath;
  }

  public static BoardConfig loadBoard(String boardName) throws IOException {
    Path boardPath = getBoardsDirectory().resolve(boardName + ".json");
    return JsonHandler.readFromJson(boardPath.toString(), BoardConfig.class);
  }
}