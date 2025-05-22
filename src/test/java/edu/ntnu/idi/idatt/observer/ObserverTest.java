package edu.ntnu.idi.idatt.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ObserverTest {

  // Positive Tests

  @Test
  public void testConstructorAndGetters() {
    String type = "MOVE";
    Integer data = 42;

    GameEvent event = new GameEvent(type, data);

    assertEquals(type, event.getType());
    assertEquals(data, event.getData());
  }

  @Test
  public void testSetters() {
    GameEvent event = new GameEvent("INITIAL", "initial value");

    String newType = "UPDATED";
    String newData = "updated value";

    event.setType(newType);
    event.setData(newData);

    assertEquals(newType, event.getType());
    assertEquals(newData, event.getData());
  }

  @Test
  public void testWithDifferentDataTypes() {
    // Test with Integer
    GameEvent event1 = new GameEvent("SCORE", 100);
    assertTrue(event1.getData() instanceof Integer);
    assertEquals(100, event1.getData());

    // Test with custom object
    Player player = new Player("Test Player");
    GameEvent event2 = new GameEvent("PLAYER_JOINED", player);
    assertTrue(event2.getData() instanceof Player);
    assertEquals(player, event2.getData());
  }

  @Test
  public void testWithEmptyType() {
    GameEvent event = new GameEvent("", "some data");
    assertEquals("", event.getType());
  }

  @Test
  public void testWithNullData() {
    GameEvent event = new GameEvent("NULL_EVENT", null);
    assertNull(event.getData());
  }

  // Negative Tests

  @Test
  public void testWithNullType() {
    GameEvent event = new GameEvent(null, "some data");
    assertNull(event.getType());
    // Assuming null type is handled gracefully
  }

  @Test
  public void testWithLargeType() {
    // Create a very large string
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
      sb.append("a");
    }
    String largeType = sb.toString();

    GameEvent event = new GameEvent(largeType, "data");
    assertEquals(largeType, event.getType());
    // Ensuring large types don't cause issues
  }

  @Test
  public void testTypeModification() {
    // Test that modifying the original string doesn't affect the event
    String mutableType = new String("ORIGINAL");
    GameEvent event = new GameEvent(mutableType, "data");

    // This creates a new string and doesn't affect the original
    mutableType = "CHANGED";

    assertEquals("ORIGINAL", event.getType());
  }

  // Helper class for testing with custom objects
  private static class Player {
    private String name;

    public Player(String name) {
      this.name = name;
    }

    // Equals and hashCode for proper comparison
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Player player = (Player) o;
      return name.equals(player.name);
    }
  }
}