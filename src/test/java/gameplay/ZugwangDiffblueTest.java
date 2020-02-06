package gameplay;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ZugwangDiffblueTest {
  @Test
  public void scoreTest() {
    // Arrange, Act and Assert
    assertEquals(0, Zugwang.NONE.score());
  }
}
