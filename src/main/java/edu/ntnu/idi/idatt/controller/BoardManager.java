package edu.ntnu.idi.idatt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.BoardConfig;
import edu.ntnu.idi.idatt.persistence.JsonHandler;
import javafx.scene.control.Alert;

public class BoardManager {

  public static Path getBoardsDirectory() {
    Path dataDir = Paths.get("./", "data", "boards");
    try {
      Files.createDirectories(dataDir);
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Failed to create data directory");
      alert.setContentText("Could not create the data directory for boards.");
      alert.showAndWait();
    }
    return dataDir;
  }

  public static void createStandardBoard() throws FileWriteException {
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

    // Add wormholes (entrance)
    standardBoard.addWormhole(5);
    standardBoard.addWormhole(13);
    standardBoard.addWormhole(45);

    Path boardPath = getBoardsDirectory().resolve("standard.json");
    JsonHandler.writeToJson(standardBoard, boardPath.toString());

  }

  /**
   * Creates a board with wormholes and saves it to a JSON file
   *
   * @return The path to the created JSON file
   * @throws IOException if there's an error writing the file
   */
  public static Path createWormholeBoard() throws FileWriteException {
    BoardConfig wormholeBoard = new BoardConfig(
        "Wormhole Board",
        "A 10x10 board with wormholes, snakes, and ladders",
        10, 10);

    // Add custom ladders (start -> end)
    wormholeBoard.addLadder(4, 23);
    wormholeBoard.addLadder(14, 35);
    wormholeBoard.addLadder(28, 49);
    wormholeBoard.addLadder(37, 65);
    wormholeBoard.addLadder(48, 68);
    wormholeBoard.addLadder(57, 82);
    wormholeBoard.addLadder(79, 96);

    // Add custom snakes (head -> tail)
    wormholeBoard.addSnake(24, 6);
    wormholeBoard.addSnake(39, 18);
    wormholeBoard.addSnake(52, 31);
    wormholeBoard.addSnake(63, 42);
    wormholeBoard.addSnake(75, 54);
    wormholeBoard.addSnake(88, 67);
    wormholeBoard.addSnake(97, 78);

    // add wormholes (entrance)
    wormholeBoard.addWormhole(10);
    wormholeBoard.addWormhole(20);
    wormholeBoard.addWormhole(30);
    wormholeBoard.addWormhole(40);

    Path boardPath = getBoardsDirectory().resolve("wormhole.json");
    JsonHandler.writeToJson(wormholeBoard, boardPath.toString());

    return boardPath;
  }

  public static BoardConfig loadBoard(String boardName) throws FileReadException {
    Path boardPath = getBoardsDirectory().resolve(boardName + ".json");
    return JsonHandler.readFromJson(boardPath.toString(), BoardConfig.class);
  }
}