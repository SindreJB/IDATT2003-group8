package edu.ntnu.idi.idatt.exceptions;

public class BoardException extends GameException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new BoardException with the specified detail message.
   *
   * @param message The detail message
   */
  public BoardException(String message) {
    super(message);
  }

  /**
   * Constructs a new BoardException with the specified detail message and cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public BoardException(String message, Throwable cause) {
    super(message, cause);
  }

}
