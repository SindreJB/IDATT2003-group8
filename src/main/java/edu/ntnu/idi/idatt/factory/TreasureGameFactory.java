package edu.ntnu.idi.idatt.factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ntnu.idi.idatt.controller.BoardManager;
import edu.ntnu.idi.idatt.model.TreasureBoard;
import edu.ntnu.idi.idatt.model.TreasureGameTile;

/**
 * Factory for creating Star of Africa game boards from JSON configurations
 */
public class TreasureGameFactory {

  /**
   * Creates a treasure board from a named configuration
   *
   * @param boardName the name of the board to create ("standard", "custom", etc.)
   * @return the created board
   * @throws IOException if there's an error reading the configuration file
   */
  public static TreasureBoard createBoard(String boardName) throws IOException {
    // Find the board configuration file
    Path boardPath = BoardManager.getBoardsDirectory().resolve("treasure-" + boardName + ".json");

    if (!Files.exists(boardPath)) {
      throw new IOException("Board configuration file not found: " + boardPath);
    }

    // Parse the JSON configuration
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(boardPath.toFile());

    return createBoardFromJson(config);
  }

  /**
   * Creates a board from a JSON configuration
   *
   * @param config the JSON configuration
   * @return the created board
   */
  public static TreasureBoard createBoardFromJson(JsonNode config) {
    // Extract board dimensions
    int rows = config.get("rows").asInt();
    int columns = config.get("columns").asInt();

    // Create a board with the specified dimensions
    TreasureBoard board = new TreasureBoard(rows, columns);

    // Set board metadata
    board.setName(config.get("name").asText());
    board.setDescription(config.get("description").asText());

    // Parse and set up the tiles based on the JSON grid
    JsonNode tilesNode = config.get("tiles");

    // Clear existing tiles (created by initialization)
    board.getTiles().clear();

    // Create tiles based on the JSON grid
    for (int row = 0; row < rows; row++) {
      JsonNode rowNode = tilesNode.get(row);
      for (int col = 0; col < columns; col++) {
        int tileType = rowNode.get(col).asInt();

        // Calculate the tile number (1-based)
        int tileNumber = row * columns + col + 1;

        // Create the tile with the appropriate type
        TreasureGameTile tile = new TreasureGameTile(tileNumber);
        tile.setTileType(tileType);

        board.getTiles().add(tile);
      }
    }

    return board;
  }

  /**
   * Tries to load a board by name, falling back to the standard board if it fails
   *
   * @param boardName the name of the board configuration file
   * @return the created board, or empty if creation fails
   */
  public static Optional<TreasureBoard> tryCreateBoard(String boardName) {
    try {
      return Optional.of(createBoard(boardName));
    } catch (IOException | IllegalArgumentException e) {
      System.err.println("Could not load treasure board '" + boardName + "': " + e.getMessage());
      try {
        return Optional.of(createBoard("standard"));
      } catch (IOException ex) {
        System.err.println("Could not create standard treasure board: " + ex.getMessage());
        return Optional.empty();
      }
    }
  }

  /**
   * Gets a tile position in the board grid from its number
   * 
   * @param tileNumber The tile number (1-based)
   * @param columns    The number of columns in the board
   * @return int[] with [row, column] coordinates
   */
  public static int[] getTilePosition(int tileNumber, int columns) {
    int row = (tileNumber - 1) / columns;
    int col = (tileNumber - 1) % columns;
    return new int[] { row, col };
  }

  /**
   * Gets a tile number from its position in the grid
   * 
   * @param row     The row position
   * @param col     The column position
   * @param columns The number of columns in the board
   * @return The tile number (1-based)
   */
  public static int getTileNumber(int row, int col, int columns) {
    return row * columns + col + 1;
  }

  /**
   * Determines if a tile is walkable based on its type
   * 
   * @param tileType The tile type value
   * @return true if the tile is walkable, false otherwise
   */
  public static boolean isWalkable(int tileType) {
    // Types 1, 2, 3 are walkable; 0 is not
    return tileType > 0;
  }

  /**
   * Determines if a tile is a treasure location
   * 
   * @param tileType The tile type value
   * @return true if the tile is a treasure location
   */
  public static boolean isTreasureLocation(int tileType) {
    return tileType == 2;
  }

  /**
   * Determines if a tile is the start position
   * 
   * @param tileType The tile type value
   * @return true if the tile is the start position
   */
  public static boolean isStartPosition(int tileType) {
    return tileType == 3;
  }
}