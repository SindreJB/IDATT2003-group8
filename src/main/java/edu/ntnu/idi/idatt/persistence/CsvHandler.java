package edu.ntnu.idi.idatt.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.Player;

/**
 * The CsvHandler class is responsible for reading and writing player data
 * from/to CSV files.
 * Each line in the CSV file contains a player's name and piece type, separated
 * by a comma.
 */
public class CsvHandler {

  /**
   * Loads players from a CSV file.
   *
   * @param filePath the path to the CSV file
   * @return a list of Player objects loaded from the file
   * @throws IOException if an I/O error occurs
   */
  public static List<Player> loadPlayersFromCsv(String filePath) throws FileReadException {
    List<Player> players = new ArrayList<>();
    Path path = Paths.get(filePath);

    if (!Files.exists(path)) {
      throw new FileReadException("File not found: " + filePath);
    }
    try {
      List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
          String name = parts[0].trim();
          String pieceType = parts[1].trim();
          players.add(new Player(name, pieceType, 1));
        }
      }
    } catch (IOException e) {
      throw new FileReadException("Error reading file: " + filePath, e);
    }

    return players;
  }

  /**
   * Saves players to a CSV file.
   * Each line in the CSV file will contain a player's name and piece type,
   * separated
   * by a comma.
   *
   * @param players  the list of Player objects to save
   * @param filePath the path to the CSV file
   * @throws IOException if an I/O error occurs
   */
  public static void savePlayersToCsv(List<Player> players, String filePath) throws FileWriteException {
    Path path = Paths.get(filePath);
    List<String> lines = new ArrayList<>();

    // Read existing lines from the file if it exists
    if (Files.exists(path)) {
      try {
        lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new FileWriteException("Error reading existing file: " + filePath, e);
      }
    }

    for (Player player : players) {
      String line = player.getName() + "," + player.getPieceType();
      // Check if the player name already exists in the lines list
      boolean nameExists = false;
      for (int i = 0; i < lines.size(); i++) {
        String existingLine = lines.get(i);
        String existingName = existingLine.split(",")[0];
        if (existingName.equals(player.getName())) {
          // Update the piece type for this player
          lines.set(i, line);
          nameExists = true;
          break;
        }
      }
      // If name doesn't exist, add as a new entry
      if (!nameExists) {
        lines.add(line);
      }
    }
    try {
      Files.write(path, lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new FileWriteException("Error writing to file: " + filePath, e);
    }
  }

}