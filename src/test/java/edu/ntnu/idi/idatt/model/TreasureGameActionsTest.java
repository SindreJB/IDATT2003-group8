package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.idi.idatt.ui.TreasureGameBoardUI;

// Note: Similar to LadderGameActionsTest, this depends on JavaFX UI components
public class TreasureGameActionsTest {
  // Mock implementation for TreasureGameBoardUI
  private static class MockTreasureGameBoardUI extends TreasureGameBoardUI {
    private boolean resetCalled = false;
    private Exception exceptionToThrow = null;
    private boolean returnNullRoot = false;
    
    public MockTreasureGameBoardUI() {
      super();
    }
    
    @Override
    public void resetGame() {
      resetCalled = true;
      if (exceptionToThrow != null) {
        throw new RuntimeException("Test exception", exceptionToThrow);
      }
    }
    
    @Override
    public javafx.scene.layout.BorderPane getRoot() {
      if (returnNullRoot) {
        return null;
      }
      return new javafx.scene.layout.BorderPane();
    }
    
    public boolean wasResetCalled() {
      return resetCalled;
    }
    
    public void setExceptionToThrow(Exception e) {
      exceptionToThrow = e;
    }
    
    public void setReturnNullRoot(boolean returnNull) {
      this.returnNullRoot = returnNull;
    }
  }
  
  private MockTreasureGameBoardUI mockBoard;
  private TreasureGameActions actions;
  
  @BeforeEach
  public void setUp() {
    mockBoard = new MockTreasureGameBoardUI();
    actions = new TreasureGameActions(mockBoard);
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
  void constructorShouldRejectNullGameBoard() {
    // Act & Assert
    NullPointerException exception = assertThrows(NullPointerException.class, 
                () -> new TreasureGameActions(null),
                "Constructor should throw NullPointerException for null gameBoard");
                
    // Verify exception message if available
    if (exception.getMessage() != null) {
        assertTrue(exception.getMessage().contains("null"), 
                "Exception message should mention null parameter");
    }
  }
  
  @Test
  void constructorShouldStoreReference() {
    TreasureGameBoardUI newMockBoard = new MockTreasureGameBoardUI();
    TreasureGameActions newActions = new TreasureGameActions(newMockBoard);
    
    // Call a method that uses the board reference
    assertDoesNotThrow(() -> newActions.restartGame(), "restartGame should work with new board reference");
    
    // Verify the correct board was called
    assertTrue(((MockTreasureGameBoardUI)newMockBoard).wasResetCalled(), 
               "resetGame should have been called on the correct board");
  }
  
  @Test
  void shouldHandleNullRootFromGameBoard() {
    // Set the mock to return null from getRoot()
    mockBoard.setReturnNullRoot(true);
    
    // Act & Assert - this test is mainly to ensure the code handles this scenario
    assertDoesNotThrow(() -> actions.restartGame(), 
                      "Actions should handle null root from game board");
  }
}
