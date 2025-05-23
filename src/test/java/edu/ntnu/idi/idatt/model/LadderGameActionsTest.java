package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private boolean returnNullScene = false;
    
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
      if (returnNullScene) {
        return null;
      }
      javafx.scene.layout.BorderPane pane = new javafx.scene.layout.BorderPane();
      if (exceptionToThrow != null) {
        // Create a scene and set it to the pane, but arrange for exception to be thrown when accessed
        pane.setUserData(exceptionToThrow);
        // We need to return a non-null pane with a null scene for the test
      } else {
        // Create a scene with the pane
        pane.setUserData(new javafx.scene.Scene(pane));
      }
      return pane;
    }
    
    public boolean wasResetCalled() {
      return resetCalled;
    }
    
    @SuppressWarnings("unused")
    public void setExceptionToThrow(Exception e) {
      exceptionToThrow = e;
    }
    
    @SuppressWarnings("unused")
    public void setReturnNullScene(boolean returnNull) {
      this.returnNullScene = returnNull;
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
  void constructorShouldRejectNullGameBoard() {
    // Act & Assert
    NullPointerException exception = assertThrows(NullPointerException.class, 
                () -> new LadderGameActions(null),
                "Constructor should throw NullPointerException for null gameBoard");
    
    // Verify exception message
    assertNotNull(exception.getMessage(), "Exception should have a message");
    assertTrue(exception.getMessage().contains("gameBoard cannot be null"), 
               "Exception message should explain the problem");
  }
  
  @Test
  void exitGameShouldHandlePlatformExceptions() {
    // This test is primarily to document the expected behavior,
    // since we can't easily test Platform.exit() without additional test infrastructure
    
    // In a real test with a framework like Mockito, you could mock the Platform class
    // and verify that exit() is called, then test exception handling
    
    // For now, we just ensure it doesn't throw any exceptions we can catch
    assertDoesNotThrow(() -> actions.exitGame(), 
                "exitGame should handle any exceptions that occur");
  }
}