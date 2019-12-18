package gameplay;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DoubleThreatDiffblueTest {
  @Test
  public void scoreTest() {
    // Arrange, Act and Assert
    assertEquals(0, DoubleThreat.NONE.score());
  }
}
