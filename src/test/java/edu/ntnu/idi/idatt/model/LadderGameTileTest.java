package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LadderGameTileTest {

  private LadderGameTile tile;
  private LadderGameTile destinationTile;

  @BeforeEach
  public void setUp() {
    tile = new LadderGameTile(10);
    destinationTile = new LadderGameTile(20);
  }

  // POSITIVE TESTS

  @Test
  public void constructorShouldSetTileNumber() {
    assertEquals(10, tile.getNumber(), "Tile number should be properly set");
  }

  @Test
  public void setSnakeShouldCorrectlySetSnakeDestination() {
    tile.setSnake(destinationTile);
    assertSame(destinationTile, tile.getSnake(), "Snake destination should be set correctly");
    assertTrue(tile.hasSnake(), "hasSnake() should return true after setting snake");
  }

  @Test
  public void setLadderShouldCorrectlySetLadderDestination() {
    tile.setLadder(destinationTile);
    assertSame(destinationTile, tile.getLadder(), "Ladder destination should be set correctly");
    assertTrue(tile.hasLadder(), "hasLadder() should return true after setting ladder");
  }

  @Test
  public void setWormholeShouldCorrectlySetWormholeDestination() {
    tile.setWormhole(destinationTile);
    assertSame(destinationTile, tile.getWormhole(), "Wormhole destination should be set correctly");
    assertTrue(tile.hasWormhole(), "hasWormhole() should return true after setting wormhole");
  }

  @Test
  public void hasActionShouldReturnTrueWhenSnakeIsSet() {
    tile.setSnake(destinationTile);
    assertTrue(tile.hasAction(), "hasAction() should return true when snake is set");
  }

  @Test
  public void hasActionShouldReturnTrueWhenLadderIsSet() {
    tile.setLadder(destinationTile);
    assertTrue(tile.hasAction(), "hasAction() should return true when ladder is set");
  }

  @Test
  public void hasActionShouldReturnTrueWhenWormholeIsSet() {
    tile.setWormhole(destinationTile);
    assertTrue(tile.hasAction(), "hasAction() should return true when wormhole is set");
  }

  @Test
  public void tileShouldSupportMultipleActions() {
    LadderGameTile snakeTile = new LadderGameTile(5);
    LadderGameTile ladderTile = new LadderGameTile(25);

    tile.setSnake(snakeTile);
    tile.setWormhole(destinationTile);

    assertTrue(tile.hasSnake(), "Tile should have a snake");
    assertTrue(tile.hasWormhole(), "Tile should have a wormhole");
    assertFalse(tile.hasLadder(), "Tile should not have a ladder");
    assertTrue(tile.hasAction(), "hasAction() should return true with multiple actions");

    assertSame(snakeTile, tile.getSnake(), "Snake destination should be set correctly");
    assertSame(destinationTile, tile.getWormhole(), "Wormhole destination should be set correctly");
  }

  // NEGATIVE TESTS

  @Test
  public void newTileShouldHaveNoActions() {
    assertFalse(tile.hasSnake(), "New tile should not have a snake");
    assertFalse(tile.hasLadder(), "New tile should not have a ladder");
    assertFalse(tile.hasWormhole(), "New tile should not have a wormhole");
    assertFalse(tile.hasAction(), "New tile should not have any actions");
  }

  @Test
  public void gettersShouldReturnNullForUnsetActions() {
    assertNull(tile.getSnake(), "getSnake() should return null when no snake is set");
    assertNull(tile.getLadder(), "getLadder() should return null when no ladder is set");
    assertNull(tile.getWormhole(), "getWormhole() should return null when no wormhole is set");
  }

  @Test
  public void settingActionToNullShouldClearAction() {
    // First set an action
    tile.setSnake(destinationTile);
    assertTrue(tile.hasSnake(), "Tile should have a snake after setting it");

    // Then clear it
    tile.setSnake(null);
    assertFalse(tile.hasSnake(), "Tile should not have a snake after clearing it");
    assertFalse(tile.hasAction(), "hasAction() should return false after clearing the only action");
  }

  @Test
  public void settingNewValueShouldOverwriteOldValue() {
    LadderGameTile firstDestination = new LadderGameTile(30);
    LadderGameTile secondDestination = new LadderGameTile(40);

    tile.setLadder(firstDestination);
    assertSame(firstDestination, tile.getLadder(), "Ladder should be set to first destination");

    tile.setLadder(secondDestination);
    assertSame(secondDestination, tile.getLadder(), "Ladder should be updated to second destination");
    assertNotSame(firstDestination, tile.getLadder(), "Ladder should no longer reference first destination");
  }

  @Test
  public void circularReferencesShouldWork() {
    LadderGameTile tileA = new LadderGameTile(1);
    LadderGameTile tileB = new LadderGameTile(2);

    // Create a circular reference - A's ladder goes to B, B's snake goes to A
    tileA.setLadder(tileB);
    tileB.setSnake(tileA);

    assertTrue(tileA.hasLadder(), "Tile A should have a ladder");
    assertTrue(tileB.hasSnake(), "Tile B should have a snake");

    assertSame(tileB, tileA.getLadder(), "A's ladder should point to B");
    assertSame(tileA, tileB.getSnake(), "B's snake should point to A");

    // Ensure we can navigate the circular reference
    assertSame(tileA, tileA.getLadder().getSnake(), "Following A's ladder and then the snake should return to A");
  }
}
