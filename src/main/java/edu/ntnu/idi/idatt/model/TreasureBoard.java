package edu.ntnu.idi.idatt.model;

public class TreasureBoard extends AbstractBoard<TreasureGameTile> {

  private final int rows;
  private final int columns;

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
    initializeTiles();
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
      int row = i / columns;
      int col = i % columns;
      int tileId = i + 1;

      TreasureGameTile tile = new TreasureGameTile(tileId);
      // Set the tile type from the board configuration
      tile.setTileType(config.getTileType(tileId));
      
      tiles.add(tile);
    }
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