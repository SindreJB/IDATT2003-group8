package edu.ntnu.idi.idatt.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.gson.JsonSyntaxException;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;

class JsonHandlerTest {

  @TempDir
  Path tempDir;

  private Path validJsonPath;
  private Path nestedDirPath;
  private Path nonExistentPath;
  private Path invalidJsonPath;
  private Path readOnlyPath;

  // Test data classes
  static class TestPerson {
    private String name;
    private int age;
    private List<String> hobbies;

    public TestPerson(String name, int age, List<String> hobbies) {
      this.name = name;
      this.age = age;
      this.hobbies = hobbies;
    }

    // Override equals for comparison in tests
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      TestPerson that = (TestPerson) o;
      return age == that.age &&
          name.equals(that.name) &&
          hobbies.equals(that.hobbies);
    }
  }

  // Test object
  private TestPerson testPerson;

  @BeforeEach
  void setUp() throws IOException {
    // Set up paths
    validJsonPath = tempDir.resolve("valid.json");
    nestedDirPath = tempDir.resolve("nested/dir/file.json");
    nonExistentPath = tempDir.resolve("nonexistent.json");
    invalidJsonPath = tempDir.resolve("invalid.json");
    readOnlyPath = tempDir.resolve("readonly.json");

    // Create test person
    testPerson = new TestPerson(
        "John Doe",
        30,
        Arrays.asList("Reading", "Hiking", "Coding"));

    // Create invalid JSON file
    Files.writeString(invalidJsonPath, "{\"name\":\"Invalid JSON\",}");

    // Create read-only file
    Files.createFile(readOnlyPath);
    File readOnlyFile = readOnlyPath.toFile();
    readOnlyFile.setReadOnly();
  }

  // POSITIVE TESTS

  @Test
  void writeToJson_ValidObject_ShouldCreateFile() throws FileWriteException, IOException {
    // Act
    JsonHandler.writeToJson(testPerson, validJsonPath.toString());

    // Assert
    assertTrue(Files.exists(validJsonPath), "JSON file should be created");
    String content = Files.readString(validJsonPath);
    assertTrue(content.contains("John Doe"), "File should contain person name");
    assertTrue(content.contains("30"), "File should contain person age");
    assertTrue(content.contains("Reading"), "File should contain hobbies");
  }

  @Test
  void writeToJson_CreatesNestedDirectories_ShouldCreateDirectories() throws FileWriteException, IOException {
    // Act
    JsonHandler.writeToJson(testPerson, nestedDirPath.toString());

    // Assert
    assertTrue(Files.exists(nestedDirPath), "Should create nested directories");
    assertTrue(Files.exists(nestedDirPath.getParent()), "Parent directories should exist");
  }

  @Test
  void writeToJson_ExistingFile_ShouldOverwrite() throws FileWriteException, IOException {
    // Arrange
    TestPerson initialPerson = new TestPerson("Initial Person", 25, Arrays.asList("Gaming"));
    JsonHandler.writeToJson(initialPerson, validJsonPath.toString());

    // Act
    JsonHandler.writeToJson(testPerson, validJsonPath.toString());

    // Assert
    String content = Files.readString(validJsonPath);
    assertTrue(content.contains("John Doe"), "File should contain updated name");
    assertFalse(content.contains("Initial Person"), "Initial data should be gone");
  }

  @Test
  void readFromJson_ValidFile_ShouldDeserializeObject() throws FileWriteException, FileReadException, IOException {
    // Arrange
    JsonHandler.writeToJson(testPerson, validJsonPath.toString());

    // Act
    TestPerson readPerson = JsonHandler.readFromJson(validJsonPath.toString(), TestPerson.class);

    // Assert
    assertEquals(testPerson, readPerson, "Deserialized object should match original");
    assertEquals("John Doe", readPerson.name, "Name should match");
    assertEquals(30, readPerson.age, "Age should match");
    assertEquals(3, readPerson.hobbies.size(), "Should have 3 hobbies");
  }

  @Test
  void roundTrip_WriteAndRead_ShouldPreserveData() throws FileWriteException, FileReadException, IOException {
    // Arrange
    List<TestPerson> people = Arrays.asList(
        new TestPerson("Alice", 28, Arrays.asList("Swimming")),
        new TestPerson("Bob", 34, Arrays.asList("Chess", "Travel")));

    // Act - write and then read back
    JsonHandler.writeToJson(people, validJsonPath.toString());
    List<?> readPeople = JsonHandler.readFromJson(validJsonPath.toString(), List.class);

    // Assert
    assertNotNull(readPeople, "Should read back a list");
    assertEquals(2, readPeople.size(), "Should have 2 people");
  }

  // NEGATIVE TESTS

  @Test
  void writeToJson_NullObject_ShouldThrowIllegalArgumentException() {
    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> JsonHandler.writeToJson(null, validJsonPath.toString()));

    assertTrue(exception.getMessage().contains("null object"),
        "Exception message should mention null object");
  }

  @Test
  void writeToJson_ReadOnlyLocation_ShouldThrowIOException() {
    // Act & Assert
    assertThrows(IOException.class, () -> JsonHandler.writeToJson(testPerson, readOnlyPath.toString()));
  }

  @Test
  void readFromJson_NullPath_ShouldThrowIllegalArgumentException() {
    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> JsonHandler.readFromJson(null, TestPerson.class));

    assertTrue(exception.getMessage().contains("null or empty"),
        "Exception message should mention null or empty path");
  }

  @Test
  void readFromJson_EmptyPath_ShouldThrowIllegalArgumentException() {
    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> JsonHandler.readFromJson("", TestPerson.class));

    assertTrue(exception.getMessage().contains("null or empty"),
        "Exception message should mention null or empty path");
  }

  @Test
  void readFromJson_NullClass_ShouldThrowIllegalArgumentException() {
    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> JsonHandler.readFromJson(validJsonPath.toString(), null));

    assertTrue(exception.getMessage().contains("Class type cannot be null"),
        "Exception message should mention null class type");
  }

  @Test
  void readFromJson_NonExistentFile_ShouldThrowIOException() {
    // Act & Assert
    Exception exception = assertThrows(IOException.class,
        () -> JsonHandler.readFromJson(nonExistentPath.toString(), TestPerson.class));

    assertTrue(exception.getMessage().contains("File not found"),
        "Exception message should mention file not found");
  }

  @Test
  void readFromJson_InvalidJson_ShouldThrowIOException() {
    // Act & Assert
    IOException exception = assertThrows(IOException.class,
        () -> JsonHandler.readFromJson(invalidJsonPath.toString(), TestPerson.class));

    assertTrue(exception.getMessage().contains("Invalid JSON format"),
        "Exception message should mention invalid JSON");
    assertTrue(exception.getCause() instanceof JsonSyntaxException,
        "Cause should be JsonSyntaxException");
  }

  @Test
  void readFromJson_IncompatibleTypes_ShouldThrowException() throws FileWriteException, IOException {
    // Arrange
    JsonHandler.writeToJson(testPerson, validJsonPath.toString());

    // Act & Assert - Try to read person as an Integer
    assertThrows(Exception.class, () -> JsonHandler.readFromJson(validJsonPath.toString(), Integer.class));
  }
}
