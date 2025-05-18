package edu.ntnu.idi.idatt.exceptions;

public class GameInitializationException extends GameException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new GameInitializationException with the specified detail
   * message.
   *
   * @param message The detail message
   */
  public GameInitializationException(String message) {
    super(message);
  }

  /**
   * Constructs a new GameInitializationException with the specified detail
   * message and
   * cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public GameInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

}
