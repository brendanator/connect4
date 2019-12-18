package gameplay;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PositionEvaluationDiffblueTest {
  @Test
  public void getYellowWinTest() {
    // Arrange, Act and Assert
    assertEquals(0L, (new PositionEvaluation(Turn.RED, 1L, 1L)).getYellowWin());
  }

  @Test
  public void getRedWinTest() {
    // Arrange, Act and Assert
    assertEquals(0L, (new PositionEvaluation(Turn.RED, 1L, 1L)).getRedWin());
  }

  @Test
  public void getPositionValueTest() {
    // Arrange, Act and Assert
    assertEquals(-7, (new PositionEvaluation(Turn.RED, 1L, 1L)).getPositionValue());
  }

  @Test
  public void toStringTest() {
    // Arrange, Act and Assert
    assertEquals("Value: -7  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 2 9",
        (new PositionEvaluation(Turn.RED, 1L, 1L)).toString());
  }

  @Test
  public void constructorTest() {
    // Arrange and Act
    PositionEvaluation actualPositionEvaluation = new PositionEvaluation(Turn.RED, 1L, 1L);

    // Assert
    long actualRedWin = actualPositionEvaluation.getRedWin();
    String actualToStringResult = actualPositionEvaluation.toString();
    int actualPositionValue = actualPositionEvaluation.getPositionValue();
    assertEquals(0L, actualRedWin);
    assertEquals(0L, actualPositionEvaluation.getYellowWin());
    assertEquals(-7, actualPositionValue);
    assertEquals("Value: -7  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 2 9",
        actualToStringResult);
  }
}
