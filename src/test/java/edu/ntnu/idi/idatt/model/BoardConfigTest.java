package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BoardConfigTest {

  private BoardConfig config;

  @BeforeEach
  void setUp() {
    config = new BoardConfig();
  }

  // POSITIVE TESTS

  @Test
  void defaultConstructorShouldInitializeLists() {
    // Assert
    assertNotNull(config.getSnakeHeads(), "Snake heads list should be initialized");
    assertNotNull(config.getSnakeTails(), "Snake tails list should be initialized");
    assertNotNull(config.getLadderStarts(), "Ladder starts list should be initialized");
    assertNotNull(config.getLadderEnds(), "Ladder ends list should be initialized");
    assertNotNull(config.getWormholeStarts(), "Wormhole starts list should be initialized");

    assertTrue(config.getSnakeHeads().isEmpty(), "Snake heads list should be empty");
    assertTrue(config.getSnakeTails().isEmpty(), "Snake tails list should be empty");
    assertTrue(config.getLadderStarts().isEmpty(), "Ladder starts list should be empty");
    assertTrue(config.getLadderEnds().isEmpty(), "Ladder ends list should be empty");
    assertTrue(config.getWormholeStarts().isEmpty(), "Wormhole starts list should be empty");
  }

  @Test
  void parameterizedConstructorShouldSetBasicProperties() {
    // Arrange
    String name = "Standard Board";
    String description = "Classic 10x10 board";
    int rows = 10;
    int columns = 10;

    // Act
    BoardConfig paramConfig = new BoardConfig(name, description, rows, columns);

    // Assert
    assertEquals(name, paramConfig.getName(), "Name should be set correctly");
    assertEquals(description, paramConfig.getDescription(), "Description should be set correctly");
    assertEquals(rows, paramConfig.getRows(), "Rows should be set correctly");
    assertEquals(columns, paramConfig.getColumns(), "Columns should be set correctly");

    // Lists should still be initialized
    assertTrue(paramConfig.getSnakeHeads().isEmpty(), "Snake heads list should be empty");
  }

  @Test
  void getterSettersShouldWorkForBasicProperties() {
    // Arrange
    String name = "Test Board";
    String description = "Test Description";
    int rows = 8;
    int columns = 12;

    // Act
    config.setName(name);
    config.setDescription(description);
    config.setRows(rows);
    config.setColumns(columns);

    // Assert
    assertEquals(name, config.getName(), "Name getter/setter should work");
    assertEquals(description, config.getDescription(), "Description getter/setter should work");
    assertEquals(rows, config.getRows(), "Rows getter/setter should work");
    assertEquals(columns, config.getColumns(), "Columns getter/setter should work");
  }

  @Test
  void listSettersShouldReplaceEntireLists() {
    // Arrange
    List<Integer> snakeHeads = Arrays.asList(25, 40, 75);
    List<Integer> snakeTails = Arrays.asList(10, 20, 30);

    // Act
    config.setSnakeHeads(snakeHeads);
    config.setSnakeTails(snakeTails);

    // Assert
    assertEquals(snakeHeads, config.getSnakeHeads(), "Snake heads should be replaced");
    assertEquals(snakeTails, config.getSnakeTails(), "Snake tails should be replaced");
    assertEquals(3, config.getSnakeHeads().size(), "Should have 3 snake heads");
  }

  @Test
  void addSnakeShouldAppendToLists() {
    // Act
    config.addSnake(25, 10);
    config.addSnake(40, 20);

    // Assert
    assertEquals(2, config.getSnakeHeads().size(), "Should have 2 snake heads");
    assertEquals(2, config.getSnakeTails().size(), "Should have 2 snake tails");
    assertEquals(25, config.getSnakeHeads().get(0), "First snake head should be 25");
    assertEquals(10, config.getSnakeTails().get(0), "First snake tail should be 10");
    assertEquals(40, config.getSnakeHeads().get(1), "Second snake head should be 40");
    assertEquals(20, config.getSnakeTails().get(1), "Second snake tail should be 20");
  }

  @Test
  void addLadderShouldAppendToLists() {
    // Act
    config.addLadder(5, 15);
    config.addLadder(30, 50);

    // Assert
    assertEquals(2, config.getLadderStarts().size(), "Should have 2 ladder starts");
    assertEquals(2, config.getLadderEnds().size(), "Should have 2 ladder ends");
    assertEquals(5, config.getLadderStarts().get(0), "First ladder start should be 5");
    assertEquals(15, config.getLadderEnds().get(0), "First ladder end should be 15");
    assertEquals(30, config.getLadderStarts().get(1), "Second ladder start should be 30");
    assertEquals(50, config.getLadderEnds().get(1), "Second ladder end should be 50");
  }

  @Test
  void addWormholeShouldAppendToList() {
    // Act
    config.addWormhole(33);
    config.addWormhole(66);

    // Assert
    assertEquals(2, config.getWormholeStarts().size(), "Should have 2 wormhole starts");
    assertEquals(33, config.getWormholeStarts().get(0), "First wormhole should be 33");
    assertEquals(66, config.getWormholeStarts().get(1), "Second wormhole should be 66");
  }

  @Test
  void shouldAllowZeroRowsAndColumns() {
    // Act
    config.setRows(0);
    config.setColumns(0);

    // Assert
    assertEquals(0, config.getRows(), "Rows should be 0");
    assertEquals(0, config.getColumns(), "Columns should be 0");
  }

  @Test
  void shouldHandleMaxIntegerValues() {
    // Act
    config.setRows(Integer.MAX_VALUE);
    config.setColumns(Integer.MAX_VALUE);

    // Assert
    assertEquals(Integer.MAX_VALUE, config.getRows(), "Rows should be MAX_VALUE");
    assertEquals(Integer.MAX_VALUE, config.getColumns(), "Columns should be MAX_VALUE");
  }

  // NEGATIVE TESTS

  @Test
  void shouldHandleNegativeRowsAndColumns() {
    // Act
    config.setRows(-5);
    config.setColumns(-10);

    // Assert
    assertEquals(-5, config.getRows(), "Negative rows should be allowed but might not be valid");
    assertEquals(-10, config.getColumns(), "Negative columns should be allowed but might not be valid");
  }

  @Test
  void shouldHandleNullNameAndDescription() {
    // Act
    config.setName(null);
    config.setDescription(null);

    // Assert
    assertNull(config.getName(), "Name should be null");
    assertNull(config.getDescription(), "Description should be null");
  }

  @Test
  void listModificationShouldReflectInGetters() {
    // Arrange
    List<Integer> snakeHeads = new ArrayList<>();
    snakeHeads.add(25);

    // Act
    config.setSnakeHeads(snakeHeads);
    // Now modify the original list
    snakeHeads.add(40);

    // Assert
    assertEquals(2, config.getSnakeHeads().size(),
        "Changes to original list should be reflected when using ArrayList");
    assertEquals(40, config.getSnakeHeads().get(1),
        "Second snake head should be 40 after external modification");
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE })
  void addSnakeShouldAcceptAnyIntegerValues(int value) {
    // Act & Assert - should not throw exception
    assertDoesNotThrow(() -> config.addSnake(value, value),
        "Adding extreme values as snake positions should not throw exception");
  }
}
