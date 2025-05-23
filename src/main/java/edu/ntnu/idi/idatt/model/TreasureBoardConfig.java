package edu.ntnu.idi.idatt.model;

/**
 * Configuration class for the treasure board layout.
 * Centralizes the board layout definition to ensure consistency
 * between UI and controller components.
 */
public class TreasureBoardConfig {

  /**
   * Standard 10x10 treasure board layout
   * 0 = Void (cannot move here)
   * 1 = Path (can move here)
   * 2 = Treasure location
   * 3 = Start position
   */
  public final int[][] STANDARD_LAYOUT = {
      { 0, 2, 1, 1, 2, 1, 2, 1, 0, 0 }, // Row 0 (tiles 1-10)
      { 1, 1, 0, 0, 0, 1, 0, 1, 1, 2 }, // Row 1 (tiles 11-20)
      { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, // Row 2 (tiles 21-30)
      { 2, 1, 1, 0, 0, 1, 0, 0, 0, 1 }, // Row 3 (tiles 31-40)
      { 0, 0, 1, 0, 0, 1, 2, 1, 1, 1 }, // Row 4 (tiles 41-50)
      { 0, 2, 1, 1, 1, 1, 0, 0, 1, 0 }, // Row 5 (tiles 51-60)
      { 0, 1, 0, 0, 1, 0, 0, 0, 2, 0 }, // Row 6 (tiles 61-70)
      { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 }, // Row 7 (tiles 71-80)
      { 0, 0, 2, 1, 1, 1, 2, 0, 0, 0 }, // Row 8 (tiles 81-90)
      { 0, 0, 0, 0, 3, 0, 0, 0, 0, 0 } // Row 9 (tiles 91-100) - Start at tile 95
  };

  public final int ROWS = 10;
  public final int COLUMNS = 10;

  /**
   * Gets the tile type for a specific tile ID
   * 
   * @param tileId The tile ID (1-100)
   * @return The tile type (0=void, 1=path, 2=treasure, 3=start)
   */
  public int getTileType(int tileId) {
    if (tileId < 1 || tileId > 100) {
      return 0; // Invalid tile
    }

    // Convert tile ID to row/col
    int row = (tileId - 1) / COLUMNS;
    int col = (tileId - 1) % COLUMNS;

    return STANDARD_LAYOUT[row][col];
  }

  /**
   * Checks if a tile is walkable
   * 
   * @param tileId The tile ID to check
   * @return True if the tile is walkable (type > 0)
   */
  public boolean isWalkable(int tileId) {
    return getTileType(tileId) > 0;
  }

  /**
   * Finds the start position (tile with type 3)
   * 
   * @return The tile ID of the start position
   */
  public int findStartPosition() {
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        if (STANDARD_LAYOUT[row][col] == 3) {
          return row * COLUMNS + col + 1;
        }
      }
    }
    return 95; // Fallback
  }

  /**
   * Converts tile ID to row/column coordinates
   * 
   * @param tileId The tile ID (1-100)
   * @return Array with [row, col] or null if invalid
   */
  public int[] getCoordinates(int tileId) {
    if (tileId < 1 || tileId > 100) {
      return null;
    }

    int row = (tileId - 1) / COLUMNS;
    int col = (tileId - 1) % COLUMNS;

    return new int[] { row, col };
  }

  /**
   * Converts row/column coordinates to tile ID
   * 
   * @param row The row (0-9)
   * @param col The column (0-9)
   * @return The tile ID (1-100) or -1 if invalid
   */
  public int getTileId(int row, int col) {
    if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
      return -1;
    }

    return row * COLUMNS + col + 1;
  }
}
