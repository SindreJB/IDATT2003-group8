package edu.ntnu.idi.idatt.filehandling;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.models.Player;

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
  public static List<Player> loadPlayersFromCsv(String filePath) throws IOException {
    List<Player> players = new ArrayList<>();
    Path path = Paths.get(filePath);

    if (!Files.exists(path)) {
      throw new FileNotFoundException("File not found: " + filePath);
    }

    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length == 2) {
        String name = parts[0].trim();
        players.add(new Player(name, 0));
      }
    }

    return players;
  }

  /**
   * Saves players to a CSV file.
   *
   * @param players  the list of Player objects to save
   * @param filePath the path to the CSV file
   * @throws IOException if an I/O error occurs
   */
  public static void savePlayersToCsv(List<Player> players, String filePath) throws IOException {
    Path path = Paths.get(filePath);
    List<String> lines = new ArrayList<>();

    for (Player player : players) {
      String line = player.getName() + ",";
      lines.add(line);
    }

    Files.write(path, lines, StandardCharsets.UTF_8);
  }

  /**
   * Creates a file chooser dialog for selecting CSV files.
   *
   * @param save true for save dialog, false for open dialog
   * @return the selected file path, or null if no file was selected
   */
  public static String showFileChooserDialog(boolean save) {
    // This method would typically use JavaFX's FileChooser or Swing's JFileChooser
    // to display a file dialog. For now, we'll return a placeholder value.
    return save ? "players.csv" : "players.csv";
  }
}
