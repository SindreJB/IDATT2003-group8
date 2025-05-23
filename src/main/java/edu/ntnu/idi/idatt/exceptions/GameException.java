package edu.ntnu.idi.idatt.exceptions;

public class GameException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new GameException with the specified detail message.
   *
   * @param message The detail message
   */
  public GameException(String message) {
    super(message);
  }

  /**
   * Constructs a new GameException with the specified detail message and cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public GameException(String message, Throwable cause) {
    super(message, cause);
  }

}
