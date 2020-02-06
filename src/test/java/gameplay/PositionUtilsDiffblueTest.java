package gameplay;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PositionUtilsDiffblueTest {
  @Test
  public void getPositionKeyTest() {
    // Arrange, Act and Assert
    assertEquals(256L, PositionUtils.getPositionKey(1, 1));
  }

  @Test
  public void cardinalityTest() {
    // Arrange, Act and Assert
    assertEquals(1, PositionUtils.cardinality(1L));
  }
}
