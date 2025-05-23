package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.idi.idatt.exceptions.InitializeLadderGameException;
import edu.ntnu.idi.idatt.ui.LadderGameBoardUI;

// Note: This test is more complex because it depends on JavaFX UI components.
// In a real environment, you might want to use a mocking framework like Mockito.
public class LadderGameActionsTest {

  // Mock implementation for LadderGameBoardUI
  private static class MockLadderGameBoardUI extends LadderGameBoardUI {
    private boolean resetCalled = false;
    private Exception exceptionToThrow = null;
    
    public MockLadderGameBoardUI() {
      super();
    }
    
    @Override
    public void resetGame() throws InitializeLadderGameException {
      resetCalled = true;
      if (exceptionToThrow != null) {
        throw new InitializeLadderGameException("Test exception", exceptionToThrow);
      }
    }
    
    @Override
    public javafx.scene.layout.BorderPane getRoot() {
      return new javafx.scene.layout.BorderPane();
    }
    
    public boolean wasResetCalled() {
      return resetCalled;
    }
    
    public void setExceptionToThrow(Exception e) {
      exceptionToThrow = e;
    }
  }
  
  private MockLadderGameBoardUI mockBoard;
  private LadderGameActions actions;
  
  @BeforeEach
  public void setUp() {
    mockBoard = new MockLadderGameBoardUI();
    actions = new LadderGameActions(mockBoard);
  }
  
  // POSITIVE TESTS
  
  @Test
  void restartGameShouldCallResetGame() {
    assertDoesNotThrow(() -> actions.restartGame(), "restartGame should not throw exceptions");
    assertTrue(mockBoard.wasResetCalled(), "resetGame should have been called on the board");
  }
  
  @Test
  void exitGameShouldBeCallable() {
    // Note: This would normally call Platform.exit() which can't be easily tested
    // In a real test with a framework like Mockito, you could verify this behavior
    assertDoesNotThrow(() -> actions.exitGame(), "exitGame should not throw exceptions");
  }
  
  // NEGATIVE TESTS
  
  @Test
  void restartGameShouldHandleExceptions() {
    // Set up mock to throw exception
    mockBoard.setExceptionToThrow(new RuntimeException("Test exception"));
    
    // The method should catch the exception and display an alert (which we can't test easily)
    assertDoesNotThrow(() -> actions.restartGame(), 
                "restartGame should catch exceptions from resetGame");
  }
  
  @Test
  void startNewGameShouldHandleExceptions() {
    // This would normally throw a NullPointerException due to our mock
    // but the method should catch it
    assertDoesNotThrow(() -> actions.startNewGame(), 
                "startNewGame should catch exceptions");
  }
}
