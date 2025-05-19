package edu.ntnu.idi.idatt.model;

/**
 * Represents a Snakes and Ladders game board with tiles arranged in a snake
 * pattern.
 * Extends the AbstractBoard class.
 */
public class LadderBoard extends AbstractBoard<LadderGameTile> {

  private final int rows;
  private final int columns;

  /**
   * Creates a new Snakes and Ladders board with the specified dimensions.
   * 
   * @param rows    Number of rows in the board
   * @param columns Number of columns in the board
   */
  public LadderBoard(int rows, int columns) {
    super();
    this.rows = rows;
    this.columns = columns;
    initializeTilesInternal();
  }

  /**
   * Initialize tiles in a snake pattern (like traditional Snakes and Ladders).
   * For example, in a 3x3 board:
   * 7 8 9
   * 6 5 4
   * 1 2 3
   */
  @Override
  protected final void initializeTiles() {
    initializeTilesInternal();
  }

  /**
   * Private helper method to initialize tiles, called from constructor.
   */
  private void initializeTilesInternal() {
    tiles.clear();
    int totalTiles = rows * columns;

    for (int i = 0; i < totalTiles; i++) {
      int row = i / columns;
      int col = i % columns;

      // For even rows, numbers go left to right
      // For odd rows, numbers go right to left
      int tileNumber = row % 2 == 0 ? row * columns + col + 1 : (row + 1) * columns - col;

      LadderGameTile tile = new LadderGameTile(tileNumber);
      // Calculate and set the x,y coordinates for UI positioning
      tiles.add(tile);
    }
  }

  /**
   * Gets a tile by its number, with specific return type of LadderGameTile.
   * Overrides the parent method to provide type specificity.
   *
   * @param number The tile number to retrieve
   * @return The LadderGameTile with the specified number
   * @throws IllegalArgumentException if the tile number is invalid
   */
  @Override
  public LadderGameTile getTile(int number) {
    return super.getTile(number);
  }

  public LadderBoard getGameBoard() {
    return this;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }
}