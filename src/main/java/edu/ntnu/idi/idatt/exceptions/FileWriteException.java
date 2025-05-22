package edu.ntnu.idi.idatt.exceptions;

public class FileWriteException extends FileHandlingException {
  /**
   * This class represents an exception that occurs when there is an error
   * writing to a file.
   * It extends the FileHandlingException class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new FileWriteException with the specified detail message.
   *
   * @param message The detail message
   */
  public FileWriteException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileWriteException with the specified detail message and
   * cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public FileWriteException(String message, Throwable cause) {
    super(message, cause);
  }

}
