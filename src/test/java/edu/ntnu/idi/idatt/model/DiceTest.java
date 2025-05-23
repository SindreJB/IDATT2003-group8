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
    Dice dice = new Dice(2);
    List<Die> diceList = dice.getDice();

    assertEquals(2, diceList.size(), "Dice should contain exactly two Die objects");
  }

  @Test
  void rollDiceShouldReturnSumOfDice() {
    Dice dice = new Dice(2);
    int result = dice.rollDice();

    assertTrue(result >= 2 && result <= 12,
        "Two dice sum should be between 2 and 12");
  }

  @RepeatedTest(100)
  void rollDiceShouldProduceValuesInExpectedRange() {
    Dice dice = new Dice(2);
    int result = dice.rollDice();

    assertTrue(result >= 2 && result <= 12,
        "Two dice sum should always be between 2 and 12");
  }

  @Test
  void multipleRollsShouldProduceVariedResults() {
    Dice dice = new Dice(2);
    Set<Integer> results = new HashSet<>();

    for (int i = 0; i < 50; i++) {
      results.add(dice.rollDice());
    }

    assertTrue(results.size() > 1,
        "Multiple rolls should produce different results");
  }

  @Test
  void getDiceShouldReturnCorrectDice() {
    Dice dice = new Dice(2);
    List<Die> diceList = dice.getDice();

    assertNotNull(diceList, "Dice list should not be null");
    assertEquals(2, diceList.size(), "Should contain exactly two dice");
  }

  @Test
  void getDiceCountShouldReturnCorrectCount() {
    // Test with different numbers of dice
    Dice dice1 = new Dice(1);
    assertEquals(1, dice1.getDiceCount(), "getDiceCount should return 1 for a single die");
    
    Dice dice2 = new Dice(2);
    assertEquals(2, dice2.getDiceCount(), "getDiceCount should return 2 for two dice");
    
    Dice dice5 = new Dice(5);
    assertEquals(5, dice5.getDiceCount(), "getDiceCount should return 5 for five dice");
  }
  
  @Test
  void constructorShouldThrowExceptionForInvalidCount() {
    Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Dice(0),
        "Constructor should throw IllegalArgumentException for dice count of 0"
    );
    
    assertEquals("Number of dice must be at least 1", exception.getMessage(),
        "Exception message should match expected message");
    
    exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Dice(-1),
        "Constructor should throw IllegalArgumentException for negative dice count"
    );
    
    assertEquals("Number of dice must be at least 1", exception.getMessage(),
        "Exception message should match expected message");
  }
}