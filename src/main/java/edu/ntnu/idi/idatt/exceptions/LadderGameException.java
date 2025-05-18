package edu.ntnu.idi.idatt.exceptions;

public class LadderGameException extends GameException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new LadderGameException with the specified detail message.
   *
   * @param message The detail message
   */
  public LadderGameException(String message) {
    super(message);
  }

  /**
   * Constructs a new LadderGameException with the specified detail message and
   * cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public LadderGameException(String message, Throwable cause) {
    super(message, cause);
  }

}
