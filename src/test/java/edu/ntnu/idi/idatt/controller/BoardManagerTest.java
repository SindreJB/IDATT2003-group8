package edu.ntnu.idi.idatt.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BoardManagerTest {

  @Test
  public void createEmptyBoardTest() {
    BoardManager.createEmptyBoard();
    Path boardPath = BoardManager.getBoardsDirectory().resolve("empty.json");
    assertTrue(Files.exists(boardPath));
  }

  @Test
  public void createBoardTest() {
    BoardManager.createStandardBoard();
    Path boardPath = BoardManager.getBoardsDirectory().resolve("standard.json");
    assertTrue(Files.exists(boardPath));
  }

}
