package edu.ntnu.idi.idatt.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import edu.ntnu.idi.idatt.exceptions.FileWriteException;

public class BoardManagerTest {

  @Test
  public void createBoardTest() throws FileWriteException {
    BoardManager.createStandardBoard();
    Path boardPath = BoardManager.getBoardsDirectory().resolve("standard.json");
    assertTrue(Files.exists(boardPath));
  }

}
