package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PlayerTest {

  @Test
  void constructorShouldSetAllProperties() {
    Player player = new Player("Alice", "TopHat", 1);

    assertEquals("Alice", player.getName());
    assertEquals("TopHat", player.getPieceType());
    assertEquals(1, player.getTileId());
  }

  @Test
  void nameAndPieceTypeShouldBeImmutable() {
    Player player = new Player("Bob", "RaceCar", 1);

    assertEquals("Bob", player.getName());
    assertEquals("RaceCar", player.getPieceType());
  }

  @Test
  void tileIdShouldBeUpdatableAfterCreation() {
    Player player = new Player("Charlie", "Dog", 1);
    player.setTileId(5);

    assertEquals(5, player.getTileId());
  }

  @Test
  void tileIdShouldAllowZeroValue() {
    Player player = new Player("David", "Ship", 0);
    assertEquals(0, player.getTileId());
  }

  @Test
  void setTileIdShouldUpdateValue() {
    Player player = new Player("Frank", "Iron", 1);

    player.setTileId(3);
    assertEquals(3, player.getTileId());

    player.setTileId(7);
    assertEquals(7, player.getTileId());
  }

  @Test
  void playersShouldBeIndependent() {
    Player player1 = new Player("Grace", "Thimble", 1);
    Player player2 = new Player("Hank", "Wheelbarrow", 1);

    player1.setTileId(10);

    assertEquals(10, player1.getTileId());
    assertEquals(1, player2.getTileId());
  }
}
