package edu.ntnu.idi.idatt.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DiceTest {

  @Test
  void constructorShouldCreateTwoDice() {
    Dice dice = new Dice();
    List<Die> diceList = dice.getDice();

    assertEquals(2, diceList.size(), "Dice should contain exactly two Die objects");
  }

  @Test
  void rollDiceShouldReturnSumOfDice() {
    Dice dice = new Dice();
    int result = dice.rollDice();

    assertTrue(result >= 2 && result <= 12,
        "Two dice sum should be between 2 and 12");
  }

  @RepeatedTest(100)
  void rollDiceShouldProduceValuesInExpectedRange() {
    Dice dice = new Dice();
    int result = dice.rollDice();

    assertTrue(result >= 2 && result <= 12,
        "Two dice sum should always be between 2 and 12");
  }

  @Test
  void multipleRollsShouldProduceVariedResults() {
    Dice dice = new Dice();
    Set<Integer> results = new HashSet<>();

    for (int i = 0; i < 50; i++) {
      results.add(dice.rollDice());
    }

    assertTrue(results.size() > 1,
        "Multiple rolls should produce different results");
  }

  @Test
  void getDiceShouldReturnCorrectDice() {
    Dice dice = new Dice();
    List<Die> diceList = dice.getDice();

    assertNotNull(diceList, "Dice list should not be null");
    assertEquals(2, diceList.size(), "Should contain exactly two dice");
  }
}