package gameplay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gui.BoardDisplay;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PositionDiffblueTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void getBoardDisplayTest() {
    // Arrange, Act and Assert
    assertTrue((new Position()).getBoardDisplay() instanceof
               PositionBoardDisplay);
  }

  @Test
  public void toStringTest() {
    // Arrange, Act and Assert
    assertEquals(
        "Value: 0  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 0 0",
        (new Position()).toString());
  }

  @Test
  public void getPossibleMovesTest() {
    // Arrange, Act and Assert
    assertEquals(7, (new Position()).getPossibleMoves().size());
  }

  @Test
  public void playMoveTest() {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new Position()).playMove(null);
  }

  @Test
  public void isGameOverTest() {
    // Arrange, Act and Assert
    assertFalse((new Position()).isGameOver());
  }

  @Test
  public void scoreTest() {
    // Arrange, Act and Assert
    assertEquals(0, (new Position()).score());
  }

  @Test
  public void zobristHashCodeTest() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Position()).zobristHashCode());
  }

  @Test
  public void constructorTest() {
    // Arrange and Act
    Position actualPosition = new Position();

    // Assert
    boolean actualIsGameOverResult = actualPosition.isGameOver();
    String actualToStringResult = actualPosition.toString();
    assertFalse(actualIsGameOverResult);
    assertEquals(7, actualPosition.getPossibleMoves().size());
    assertEquals(
        "Value: 0  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 0 0",
        actualToStringResult);
  }
}
