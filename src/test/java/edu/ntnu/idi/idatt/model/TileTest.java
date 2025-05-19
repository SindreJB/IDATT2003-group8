package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TileTest {

  // Concrete implementation of the abstract Tile class for testing
  static class TestTile extends Tile {
    public TestTile(int number) {
      super(number);
    }
  }

  @Test
  void constructorShouldSetTileNumber() {
    TestTile tile = new TestTile(5);
    assertEquals(5, tile.getNumber());
  }

  @Test
  void getNumberShouldReturnTileNumber() {
    TestTile tile = new TestTile(10);
    assertEquals(10, tile.getNumber());
  }

  @Test
  void setNumberShouldChangeTileNumber() {
    TestTile tile = new TestTile(1);

    tile.setNumber(25);
    assertEquals(25, tile.getNumber());

    tile.setNumber(99);
    assertEquals(99, tile.getNumber());
  }

  @Test
  void multipleTilesShouldBeIndependent() {
    TestTile tile1 = new TestTile(1);
    TestTile tile2 = new TestTile(2);

    tile1.setNumber(50);

    assertEquals(50, tile1.getNumber());
    assertEquals(2, tile2.getNumber());
  }
}