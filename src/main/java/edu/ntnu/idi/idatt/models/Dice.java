package edu.ntnu.idi.idatt.models;

import java.util.List;

public class Dice {

  private final List<Die> dice; // List for the option of adding more dice

  public Dice() {
    this.dice = List.of(new Die(), new Die()); // Two dice
  }

  public int rollDice() {
    return dice.stream().mapToInt(Die::roll).sum();
  }

  public List<Die> getDice() {
    return dice;
  }
}