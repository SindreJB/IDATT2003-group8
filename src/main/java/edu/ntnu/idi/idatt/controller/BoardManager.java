package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.BoardConfig;
import edu.ntnu.idi.idatt.persistence.JsonHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                10, 9
        );

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
                10, 9
        );

        // Add ladders (from bottom to top)
        standardBoard.addLadder(2, 38);
        standardBoard.addLadder(5, 44);
        standardBoard.addLadder(6, 45);
        standardBoard.addLadder(7, 46);
        standardBoard.addLadder(8, 47);
        standardBoard.addLadder(9, 48);

        // Add snakes (from head to tail)
        standardBoard.addSnake(16, 6);

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
                10, 10
        );

        // Add custom ladders (start -> end)
        customBoard.addLadder(4, 25);
        customBoard.addLadder(13, 46);
        customBoard.addLadder(33, 49);
        customBoard.addLadder(42, 63);
        customBoard.addLadder(50, 69);
        customBoard.addLadder(62, 81);
        customBoard.addLadder(74, 92);

        // Add custom snakes (head -> tail)
        customBoard.addSnake(27, 5);
        customBoard.addSnake(40, 3);
        customBoard.addSnake(43, 18);
        customBoard.addSnake(54, 31);
        customBoard.addSnake(66, 45);
        customBoard.addSnake(76, 58);
        customBoard.addSnake(89, 53);
        customBoard.addSnake(99, 41);

        Path boardPath = getBoardsDirectory().resolve(boardName + ".json");
        JsonHandler.writeToJson(customBoard, boardPath.toString());
        System.out.println("Custom board saved to: " + boardPath);

        return boardPath;
    }

    public static BoardConfig loadBoard(String boardName) throws IOException {
        Path boardPath = getBoardsDirectory().resolve(boardName + ".json");
        return JsonHandler.readFromJson(boardPath.toString(), BoardConfig.class);
    }
}