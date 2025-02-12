package edu.ntnu.idi.idatt.models;

import java.util.Random;

public class Die {

  private int lastRolledValue;
  private Random random;

  public Die() {
    this.random = new Random();
  }

  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }
}