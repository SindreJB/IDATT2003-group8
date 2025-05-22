package edu.ntnu.idi.idatt.exceptions;

public class InitializeLadderGameException extends GameInitializationException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new InitializeLadderGameException with the specified detail
   * message.
   *
   * @param message The detail message
   */
  public InitializeLadderGameException(String message) {
    super(message);
  }

  /**
   * Constructs a new InitialIzeLadderGameException with the specified detail
   * message and cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public InitializeLadderGameException(String message, Throwable cause) {
    super(message, cause);
  }

}
