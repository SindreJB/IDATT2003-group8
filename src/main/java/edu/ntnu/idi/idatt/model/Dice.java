package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Dice} class represents a collection of dice that can be rolled
 * together.
 * It provides methods to roll the dice and retrieve the list of dice.
 * 
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Dice dice = new Dice();
 * int result = dice.rollDice();
 * List<Die> diceList = dice.getDice();
 * }</pre>
 * 
 * @author
 */
public class Dice {

  private final List<Die> dice; // List for the option of adding more dice

  /**
   * Constructs a Dice object with two Die objects.
   * The Dice object represents a pair of dice.
   */
  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Number of dice must be at least 1");
    }
    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }

  }

  /**
   * Rolls all dice in the collection and returns the sum of their values.
   *
   * @return the sum of the values of all rolled dice
   */
  public int rollDice() {
    return dice.stream().mapToInt(Die::roll).sum();
  }

  /**
   * Returns the list of dice.
   *
   * @return a list of {@link Die} objects representing the dice.
   */
  public List<Die> getDice() {
    return dice;
  }
}