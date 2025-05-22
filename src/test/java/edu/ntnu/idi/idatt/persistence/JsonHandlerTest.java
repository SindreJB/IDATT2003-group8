package edu.ntnu.idi.idatt.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
  private Path nonExistentPath;
  private Path invalidJsonPath;
  private Path readOnlyPath;

  // Test data classes
  static class TestPerson {
    private final String name;
    private final int age;
    private final List<String> hobbies;

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

  // NEGATIVE TESTS

  @Test
  void writeToJson_NullObject_ShouldThrowIllegalArgumentException() {
    // Act & Assert
    Exception exception = assertThrows(FileWriteException.class,
        () -> JsonHandler.writeToJson(null, validJsonPath.toString()));

    assertTrue(exception.getMessage().contains("null object"),
        "Exception message should mention null object");
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
  void readFromJson_NonExistentFile_ShouldThrowFileReadException() {
    // Act & Assert
    Exception exception = assertThrows(FileReadException.class,
        () -> JsonHandler.readFromJson(nonExistentPath.toString(), TestPerson.class));

    assertTrue(exception.getMessage().contains("File not found"),
        "Exception message should mention file not found");
  }

  @Test
  void readFromJson_InvalidJson_ShouldThrowFileReadException() {
    // Act & Assert
    FileReadException exception = assertThrows(FileReadException.class,
        () -> JsonHandler.readFromJson(invalidJsonPath.toString(), TestPerson.class));

    assertTrue(exception.getMessage().contains("Invalid JSON format"),
        "Exception message should mention invalid JSON");
    assertTrue(exception.getCause() instanceof JsonSyntaxException,
        "Cause should be JsonSyntaxException");
  }

  @Test
  void readFromJson_IncompatibleTypes_ShouldThrowException() throws FileWriteException, FileReadException, IOException {
    // Arrange
    JsonHandler.writeToJson(testPerson, validJsonPath.toString());

    // Act & Assert - Try to read person as an Integer
    assertThrows(Exception.class, () -> JsonHandler.readFromJson(validJsonPath.toString(), Integer.class));
  }
}
