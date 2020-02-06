package gameplay;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DifficultyDiffblueTest {
  @Test
  public void getDepthTest() {
    // Arrange, Act and Assert
    assertEquals(2, Difficulty.EASY.getDepth());
  }

  @Test
  public void getNextTest() {
    // Arrange, Act and Assert
    assertEquals(Difficulty.MEDIUM, Difficulty.EASY.getNext());
  }
}
