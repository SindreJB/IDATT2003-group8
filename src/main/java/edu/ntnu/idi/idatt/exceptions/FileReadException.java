package edu.ntnu.idi.idatt.exceptions;

public class FileReadException extends FileHandlingException {
  /**
   * This class represents an exception that occurs when there is an error
   * reading from a file.
   * It extends the FileHandlingException class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new FileReadException with the specified detail message.
   *
   * @param message The detail message
   */
  public FileReadException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileReadException with the specified detail message and
   * cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public FileReadException(String message, Throwable cause) {
    super(message, cause);
  }

}
