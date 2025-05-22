package edu.ntnu.idi.idatt.exceptions;

public class FileHandlingException extends Exception {
  /**
   * This class represents an exception that occurs during file handling
   * operations,
   * such as reading or writing files.
   * It extends the Exception class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new FileHandlingException with the specified detail message.
   *
   * @param message The detail message
   */
  public FileHandlingException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileHandlingException with the specified detail message and
   * cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public FileHandlingException(String message, Throwable cause) {
    super(message, cause);
  }

}
