package gameplay;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ZobristHashingDiffblueTest {
  @Test
  public void putPositionTest() {
    // Arrange
    Position position = new Position();

    // Act
    ZobristHashing.putPosition(1L, position);

    // Assert
    assertEquals(7, position.getPossibleMoves().get(1).getPossibleMoves().get(0).getPossibleMoves().size());
  }

}
