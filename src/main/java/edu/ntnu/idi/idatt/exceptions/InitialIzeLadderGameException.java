package edu.ntnu.idi.idatt.exceptions;

public class InitialIzeLadderGameException extends GameInitializationException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new InitialIzeLadderGameException with the specified detail
   * message.
   *
   * @param message The detail message
   */
  public InitialIzeLadderGameException(String message) {
    super(message);
  }

  /**
   * Constructs a new InitialIzeLadderGameException with the specified detail
   * message and cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public InitialIzeLadderGameException(String message, Throwable cause) {
    super(message, cause);
  }

}
