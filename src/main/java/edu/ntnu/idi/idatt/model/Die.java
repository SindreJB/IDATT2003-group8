package edu.ntnu.idi.idatt.model;

import java.util.Random;

/**
 * The Die class represents a single die that can be rolled to generate a random
 * number between 1 and 6.
 * It uses an instance of the Random class to generate the random numbers.
 */
public class Die {

  private final Random random;

  /**
   * Constructs a new Die object.
   * Initializes the random number generator.
   */
  public Die() {
    this.random = new Random();
  }

  /**
   * Rolls the die and returns a random integer between 1 and 6 (inclusive).
   *
   * @return a random integer between 1 and 6
   */
  public int roll() {
    return random.nextInt(6) + 1;
  }
}