package edu.ntnu.idi.idatt.models;

import java.util.Random;

public class Die {

  private final Random random;

  public Die() {
    this.random = new Random();
  }

  public int roll() {
    return random.nextInt(6) + 1;
  }
}