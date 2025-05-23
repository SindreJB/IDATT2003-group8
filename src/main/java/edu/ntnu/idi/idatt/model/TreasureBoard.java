package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreasureBoard extends AbstractBoard<TreasureGameTile> {

  private final int rows;
  private final int columns;
  private int treasureTileId = -1;

  /**
   * Creates a new Treasure board with the specified dimensions.
   *
   * @param rows    Number of rows in the board
   * @param columns Number of columns in the board
   */
  public TreasureBoard(int rows, int columns) {
    super();
    this.rows = rows;
    this.columns = columns;
  }

  /**
   * Initialize the board with a random treasure location.
   * Should be called after board creation.
   */
  public void initializeBoardWithTreasure() {
    initializeTiles();
    assignRandomTreasure();
  }

  /**
   * Assign a random treasure to one of the type 2 tiles.
   */
  public void assignRandomTreasure() {
    List<TreasureGameTile> treasureTiles = new ArrayList<>();

    // Find all treasure tiles (type 2)
    for (TreasureGameTile tile : tiles) {
      if (tile.getTileType() == 2) {
        treasureTiles.add(tile);
      }
    }

    if (!treasureTiles.isEmpty()) {
      Random random = new Random();
      int randomIndex = random.nextInt(treasureTiles.size());
      TreasureGameTile chosenTile = treasureTiles.get(randomIndex);
      chosenTile.setHasTreasure(true);
      treasureTileId = chosenTile.getNumber();

    }
  }

  /**
   * Initialize tiles in a tree-like pattern.
   * Uses TreasureBoardConfig for tile type information.
   */
  @Override
  protected void initializeTiles() {
    tiles.clear();
    int totalTiles = rows * columns;
    TreasureBoardConfig config = new TreasureBoardConfig();

    for (int i = 0; i < totalTiles; i++) {
      int tileId = i + 1;

      TreasureGameTile tile = new TreasureGameTile(tileId);
      // Set the tile type from the board configuration
      int tileType = config.getTileType(tileId);
      tile.setTileType(tileType);

      // Add to the list of all tiles
      tiles.add(tile);
    }
  }

  /**
   * Gets the ID of the tile that contains the real treasure
   * 
   * @return The tile ID of the real treasure, or -1 if none exists
   */
  public int getTreasureTileId() {
    return treasureTileId;
  }

  @Override
  public TreasureGameTile getTile(int number) {
    return super.getTile(number);
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }
}