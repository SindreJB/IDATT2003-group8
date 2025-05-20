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
   * 
   * for example in a 5x5 board:
   * 
   * 8 9 0 0 0
   * 0 4 5 6 7
   * 0 0 2 3 0
   * 0 0 1 0 0
   */
  @Override
  protected void initializeTiles() {
    tiles.clear();
    int totalTiles = rows * columns;

    for (int i = 0; i < totalTiles; i++) {
      int row = i / columns;
      int col = i % columns;

      // For even rows, numbers go left to right
      // For odd rows, numbers go right to left
      int tileNumber = row % 2 == 0 ? row * columns + col + 1 : (row + 1) * columns - col;

      TreasureGameTile tile = new TreasureGameTile(tileNumber);
      // Calculate and set the x,y coordinates for UI positioning
      tiles.add(tile);
    }
  }

  @Override
  public TreasureGameTile getTile(int number) {
    return super.getTile(number);
  }

  public TreasureBoard getGameBoard() {
    return this;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

}
