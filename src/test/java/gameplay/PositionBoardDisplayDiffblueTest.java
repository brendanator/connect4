package gameplay;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PositionBoardDisplayDiffblueTest {
  @Rule public ExpectedException thrown = ExpectedException.none();
  @Test
  public void isYellowWinTest() {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new PositionBoardDisplay(Turn.RED, 1L, 1L)).isYellowWin(null, null);
  }
  @Test
  public void isRedWinTest() {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new PositionBoardDisplay(Turn.RED, 1L, 1L)).isRedWin(null, null);
  }
  @Test
  public void isYellowTest() {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new PositionBoardDisplay(Turn.RED, 1L, 1L)).isYellow(null, null);
  }
  @Test
  public void isRedTest() {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new PositionBoardDisplay(Turn.RED, 1L, 1L)).isRed(null, null);
  }
}
