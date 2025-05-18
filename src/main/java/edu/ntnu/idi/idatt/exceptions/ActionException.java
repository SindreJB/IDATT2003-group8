package edu.ntnu.idi.idatt.exceptions;

public class ActionException extends LadderGameException {
  /**
   * This class represents an exception that occurs during the execution of an
   * action in the game.
   * It extends the LadderGameException class.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new ActionException with the specified detail message.
   *
   * @param message The detail message
   */
  public ActionException(String message) {
    super(message);
  }

  /**
   * Constructs a new ActionException with the specified detail message and cause.
   *
   * @param message The detail message
   * @param cause   The cause of the exception
   */
  public ActionException(String message, Throwable cause) {
    super(message, cause);
  }

}
