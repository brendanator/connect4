package gameplay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import org.junit.Test;

public class PlayDiffblueTest {
  @Test
  public void findNextMoveTest() {
    // Arrange
    Play play = new Play();

    // Act
    Position actualFindNextMoveResult = play.findNextMove(new Position(), Difficulty.EASY);

    // Assert
    boolean actualIsGameOverResult = actualFindNextMoveResult.isGameOver();
    String actualToStringResult = actualFindNextMoveResult.toString();
    ArrayList<Position> possibleMoves = actualFindNextMoveResult.getPossibleMoves();
    assertFalse(actualIsGameOverResult);
    assertEquals(7, possibleMoves.size());
    Position getResult = possibleMoves.get(0);
    Position getResult1 = possibleMoves.get(1);
    Position getResult2 = possibleMoves.get(2);
    assertEquals("Value: 6  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 6 0",
        actualToStringResult);
    boolean actualIsGameOverResult1 = getResult1.isGameOver();
    String actualToStringResult1 = getResult1.toString();
    ArrayList<Position> possibleMoves1 = getResult1.getPossibleMoves();
    boolean actualIsGameOverResult2 = getResult.isGameOver();
    String actualToStringResult2 = getResult.toString();
    ArrayList<Position> possibleMoves2 = getResult.getPossibleMoves();
    boolean actualIsGameOverResult3 = getResult2.isGameOver();
    String actualToStringResult3 = getResult2.toString();
    assertEquals(7, getResult2.getPossibleMoves().size());
    assertEquals("Value: -13  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 6 19",
        actualToStringResult3);
    assertEquals("Value: -7  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 6 13",
        actualToStringResult1);
    assertEquals(7, possibleMoves2.size());
    assertFalse(actualIsGameOverResult3);
    assertEquals(7, possibleMoves1.size());
    assertFalse(actualIsGameOverResult1);
    assertFalse(actualIsGameOverResult2);
    assertEquals("Value: -2  DoubleThreat: NONE  Zugwang: NONE  Threats: 0, 0  Minor Threats: 0, 0  Blockers: 6 8",
        actualToStringResult2);
    assertEquals(7, possibleMoves1.get(0).getPossibleMoves().size());
  }
}
