package edu.ntnu.idi.idatt.model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class DieTest {

  @Test
  void rollShouldReturnValueBetweenOneAndSix() {
    Die die = new Die();
    for (int i = 0; i < 100; i++) {
      int result = die.roll();
      assertTrue(result >= 1 && result <= 6, "Die roll should be between 1 and 6");
    }
  }

  @Test
  void dieRollsShouldProduceAllPossibleValuesEventually() {
    Die die = new Die();
    Set<Integer> observedValues = new HashSet<>();

    for (int i = 0; i < 100; i++) {
      observedValues.add(die.roll());
      if (observedValues.size() == 6)
        break;
    }

    assertEquals(6, observedValues.size(), "Die should eventually produce all values 1-6");
  }

  @Test
  void multipleDieShouldBeIndependent() {
    Die die1 = new Die();
    Die die2 = new Die();

    boolean foundDifferentResults = false;
    for (int i = 0; i < 10 && !foundDifferentResults; i++) {
      int roll1 = die1.roll();
      int roll2 = die2.roll();
      if (roll1 != roll2) {
        foundDifferentResults = true;
      }
    }

    assertTrue(foundDifferentResults, "Two dice should eventually roll different values");
  }
}