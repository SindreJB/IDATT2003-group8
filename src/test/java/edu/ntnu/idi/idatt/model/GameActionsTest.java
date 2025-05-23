package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameActionsTest {

  // Mock implementation for testing
  private static class MockGameActions implements GameActions {
    private boolean restartCalled = false;
    private boolean newGameCalled = false;
    private boolean exitCalled = false;
    
    @Override
    public void restartGame() {
      restartCalled = true;
    }
    
    @Override
    public void startNewGame() {
      newGameCalled = true;
    }
    
    @Override
    public void exitGame() {
      exitCalled = true;
    }
    
    public boolean wasRestartCalled() {
      return restartCalled;
    }
    
    public boolean wasNewGameCalled() {
      return newGameCalled;
    }
    
    public boolean wasExitCalled() {
      return exitCalled;
    }
    
    public void reset() {
      restartCalled = false;
      newGameCalled = false;
      exitCalled = false;
    }
  }
  
  private MockGameActions actions;
  
  @BeforeEach
  public void setUp() {
    actions = new MockGameActions();
  }
  
  // POSITIVE TESTS
  
  @Test
  void restartGameShouldBeCallable() {
    assertDoesNotThrow(() -> actions.restartGame(), "restartGame should not throw exceptions");
    assertTrue(actions.wasRestartCalled(), "restartGame should have been called");
    assertEquals(false, actions.wasNewGameCalled(), "newGame should not have been called");
    assertEquals(false, actions.wasExitCalled(), "exitGame should not have been called");
  }
  
  @Test
  void startNewGameShouldBeCallable() {
    assertDoesNotThrow(() -> actions.startNewGame(), "startNewGame should not throw exceptions");
    assertTrue(actions.wasNewGameCalled(), "startNewGame should have been called");
    assertEquals(false, actions.wasRestartCalled(), "restartGame should not have been called");
    assertEquals(false, actions.wasExitCalled(), "exitGame should not have been called");
  }
  
  @Test
  void exitGameShouldBeCallable() {
    assertDoesNotThrow(() -> actions.exitGame(), "exitGame should not throw exceptions");
    assertTrue(actions.wasExitCalled(), "exitGame should have been called");
    assertEquals(false, actions.wasRestartCalled(), "restartGame should not have been called");
    assertEquals(false, actions.wasNewGameCalled(), "newGame should not have been called");
  }
  
  @Test
  void multipleActionsShouldBeCallableInSequence() {
    actions.restartGame();
    assertTrue(actions.wasRestartCalled(), "restartGame should have been called");
    
    actions.reset();
    actions.startNewGame();
    assertTrue(actions.wasNewGameCalled(), "startNewGame should have been called");
    
    actions.reset();
    actions.exitGame();
    assertTrue(actions.wasExitCalled(), "exitGame should have been called");
  }
  
  // NEGATIVE TESTS
  
  @Test
  void methodsShouldHandleMultipleCalls() {
    // Call each method twice
    actions.restartGame();
    actions.restartGame();
    assertTrue(actions.wasRestartCalled(), "restartGame flag should be true after multiple calls");
    
    actions.reset();
    actions.startNewGame();
    actions.startNewGame();
    assertTrue(actions.wasNewGameCalled(), "startNewGame flag should be true after multiple calls");
    
    actions.reset();
    actions.exitGame();
    actions.exitGame();
    assertTrue(actions.wasExitCalled(), "exitGame flag should be true after multiple calls");
  }
}
