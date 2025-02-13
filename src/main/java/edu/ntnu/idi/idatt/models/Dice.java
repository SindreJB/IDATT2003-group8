package edu.ntnu.idi.idatt.models;

import java.util.List;
import java.util.stream.IntStream;

public class Dice {

  private final List<Die> dice;
  private final int[] result;
  private final int sum;
  private final boolean isEqual;

  public Dice() {
    this.dice = List.of(new Die(), new Die());
    this.result = dice.stream().mapToInt(Die::roll).toArray();
    this.sum = IntStream.of(result).sum();
    this.isEqual = IntStream.of(result).allMatch(n -> n == result[0]);
    
  }

  public boolean getEquals() {
      return this.isEqual;
  }

  public int getSum() {
    return this.sum;
  }

  public int[] getResult() {
    return  this.result;
  }

  public List<Die> getDice() {
    return dice;
  }

}