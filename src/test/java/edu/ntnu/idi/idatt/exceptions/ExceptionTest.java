package edu.ntnu.idi.idatt.exceptions;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ExceptionTest {

  // FileReadException Tests

  @Test
  void fileReadExceptionShouldExtendIOException() {
    // Arrange & Act
    FileReadException exception = new FileReadException("Test message");

    // Assert
    assertTrue(exception instanceof Exception,
        "FileReadException should extend Exception");
  }

  @Test
  void fileReadExceptionShouldStoreMessage() {
    // Arrange
    String message = "Unable to read file";

    // Act
    FileReadException exception = new FileReadException(message);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
  }

  @Test
  void fileReadExceptionShouldStoreCause() {
    // Arrange
    String message = "Unable to read file";
    IOException cause = new IOException("Original error");

    // Act
    FileReadException exception = new FileReadException(message, cause);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
    assertEquals(cause, exception.getCause(),
        "Exception should store the cause");
  }

  // FileWriteException Tests

  @Test
  void fileWriteExceptionShouldExtendIOException() {
    // Arrange & Act
    FileWriteException exception = new FileWriteException("Test message");

    // Assert
    assertTrue(exception instanceof Exception,
        "FileWriteException should extend Exception");
  }

  @Test
  void fileWriteExceptionShouldStoreMessage() {
    // Arrange
    String message = "Unable to write to file";

    // Act
    FileWriteException exception = new FileWriteException(message);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
  }

  @Test
  void fileWriteExceptionShouldStoreCause() {
    // Arrange
    String message = "Unable to write to file";
    IOException cause = new IOException("Original error");

    // Act
    FileWriteException exception = new FileWriteException(message, cause);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
    assertEquals(cause, exception.getCause(),
        "Exception should store the cause");
  }

  // InitializeLadderGameException Tests

  @Test
  void initializeLadderGameExceptionShouldExtendException() {
    // Arrange & Act
    InitializeLadderGameException exception = new InitializeLadderGameException("Test message");

    // Assert
    assertTrue(exception instanceof Exception,
        "InitializeLadderGameException should extend Exception");
  }

  @Test
  void initializeLadderGameExceptionShouldStoreMessage() {
    // Arrange
    String message = "Unable to initialize ladder game";

    // Act
    InitializeLadderGameException exception = new InitializeLadderGameException(message);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
  }

  @Test
  void initializeLadderGameExceptionShouldStoreCause() {
    // Arrange
    String message = "Unable to initialize ladder game";
    Exception cause = new Exception("Configuration error");

    // Act
    InitializeLadderGameException exception = new InitializeLadderGameException(message, cause);

    // Assert
    assertEquals(message, exception.getMessage(),
        "Exception should store the message");
    assertEquals(cause, exception.getCause(),
        "Exception should store the cause");
  }
}