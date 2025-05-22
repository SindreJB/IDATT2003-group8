package edu.ntnu.idi.idatt.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.Player;

public class CsvHandlerTest {

  @TempDir
  Path tempDir;

  private Path validCsvPath;
  private Path emptyFilePath;
  private Path invalidFormatPath;
  private Path outputPath;
  private Path nonExistentPath;

  private List<Player> testPlayers;

  @BeforeEach
  void setUp() throws IOException {
    // Create test paths
    validCsvPath = tempDir.resolve("valid_players.csv");
    emptyFilePath = tempDir.resolve("empty.csv");
    invalidFormatPath = tempDir.resolve("invalid_format.csv");
    outputPath = tempDir.resolve("output.csv");
    nonExistentPath = tempDir.resolve("nonexistent.csv");

    // Create test players
    testPlayers = Arrays.asList(
        new Player("Alice", "#FF0000", 1),
        new Player("Bob", "#0000FF", 1),
        new Player("Charlie", "#00FF00", 1));

    // Create test files
    List<String> validCsvContent = Arrays.asList(
        "Alice,#FF0000",
        "Bob,#0000FF",
        "Charlie,#00FF00");

    List<String> invalidFormatContent = Arrays.asList(
        "Alice,#FF0000",
        "InvalidLine",
        "Charlie,#00FF00,ExtraField",
        "");

    Files.write(validCsvPath, validCsvContent);
    Files.write(emptyFilePath, new ArrayList<>());
    Files.write(invalidFormatPath, invalidFormatContent);
  }

  @AfterEach
  void cleanUp() {
    // Clean up any created files
    try {
      Files.deleteIfExists(outputPath);
    } catch (IOException e) {
      // Ignore, just clean-up
    }
  }

  // POSITIVE TESTS

  @Test
  void loadPlayersFromCsv_ValidFile_ShouldLoadAllPlayers() throws FileReadException {
    // Act
    List<Player> players = CsvHandler.loadPlayersFromCsv(validCsvPath.toString());

    // Assert
    assertEquals(3, players.size(), "Should load 3 players");
    assertEquals("Alice", players.get(0).getName(), "First player should be Alice");
    assertEquals("#FF0000", players.get(0).getPieceType(), "Alice's piece type should be #FF0000");
    assertEquals(1, players.get(0).getTileId(), "All players should start at position 1");
  }

  @Test
  void loadPlayersFromCsv_EmptyFile_ShouldReturnEmptyList() throws FileReadException {
    // Act
    List<Player> players = CsvHandler.loadPlayersFromCsv(emptyFilePath.toString());

    // Assert
    assertTrue(players.isEmpty(), "Should return empty list for empty file");
  }

  @Test
  void loadPlayersFromCsv_InvalidFormat_ShouldSkipInvalidLines() throws FileReadException {
    // Act
    List<Player> players = CsvHandler.loadPlayersFromCsv(invalidFormatPath.toString());

    // Assert
    assertEquals(1, players.size(), "Should only load valid lines");
    assertEquals("Alice", players.get(0).getName(), "Should load Alice");
  }

  @Test
  void savePlayersToCsv_ValidData_ShouldSaveAllPlayers() throws FileWriteException, IOException {
    // Act
    CsvHandler.savePlayersToCsv(testPlayers, outputPath.toString());

    // Assert
    assertTrue(Files.exists(outputPath), "File should be created");
    List<String> lines = Files.readAllLines(outputPath);
    assertEquals(3, lines.size(), "File should contain 3 lines");
    assertEquals("Alice,#FF0000", lines.get(0), "First line should be Alice's data");
  }

  @Test
  void savePlayersToCsv_EmptyList_ShouldCreateEmptyFile() throws FileWriteException, IOException {
    // Act
    CsvHandler.savePlayersToCsv(new ArrayList<>(), outputPath.toString());

    // Assert
    assertTrue(Files.exists(outputPath), "File should be created");
    List<String> lines = Files.readAllLines(outputPath);
    assertTrue(lines.isEmpty(), "File should be empty");
  }

  @Test
  void loadAndSavePlayersToCsv_ShouldPreservePlayerData() throws FileWriteException, IOException, FileReadException {
    // Act - first save
    CsvHandler.savePlayersToCsv(testPlayers, outputPath.toString());

    // Then load back
    List<Player> loadedPlayers = CsvHandler.loadPlayersFromCsv(outputPath.toString());

    // Assert
    assertEquals(testPlayers.size(), loadedPlayers.size(), "Should preserve the number of players");
    for (int i = 0; i < testPlayers.size(); i++) {
      assertEquals(testPlayers.get(i).getName(), loadedPlayers.get(i).getName(),
          "Player names should match");
      assertEquals(testPlayers.get(i).getPieceType(), loadedPlayers.get(i).getPieceType(),
          "Piece types should match");
    }
  }

  // NEGATIVE TESTS

  @Test
  void loadPlayersFromCsv_FileNotFound_ShouldThrowFileReadException() {
    // Act & Assert
    assertThrows(FileReadException.class, () -> CsvHandler.loadPlayersFromCsv(nonExistentPath.toString()),
        "Should throw FileReadException for non-existent file");
  }

  @Test
  void loadPlayersFromCsv_NullPath_ShouldThrowNullPointerException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> CsvHandler.loadPlayersFromCsv(null),
        "Should throw NullPointerException for null path");
  }

  @Test
  void savePlayersToCsv_NullPlayers_ShouldThrowNullPointerException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> CsvHandler.savePlayersToCsv(null, outputPath.toString()),
        "Should throw NullPointerException for null players list");
  }

  @Test
  void savePlayersToCsv_NullPath_ShouldThrowNullPointerException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> CsvHandler.savePlayersToCsv(testPlayers, null),
        "Should throw NullPointerException for null path");
  }

  @Test
  void savePlayersToCsv_InvalidPath_ShouldThrowFileWriteException() {
    // Arrange
    String invalidPath = tempDir.resolve("nonexistent_dir/output.csv").toString();

    // Act & Assert
    assertThrows(FileWriteException.class, () -> CsvHandler.savePlayersToCsv(testPlayers, invalidPath),
        "Should throw FileWriteException for invalid path");
  }

  @Test
  void savePlayersToCsv_ReadOnlyLocation_ShouldThrowFileWriteException() throws IOException {
    // Arrange - create a file and make it read-only
    Path readOnlyPath = tempDir.resolve("readonly.csv");
    Files.createFile(readOnlyPath);
    File readOnlyFile = readOnlyPath.toFile();
    readOnlyFile.setReadOnly();

    // Act & Assert
    assertThrows(FileWriteException.class, () -> CsvHandler.savePlayersToCsv(testPlayers, readOnlyPath.toString()),
        "Should throw FileWriteException when writing to read-only file");
  }
}
