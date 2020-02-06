package gameplay;

import java.util.HashMap;
import java.util.Random;

public class ZobristHashing {
  private static final long[] RED_ZOBRIST_POSITIONS = createZobristPositions();
  private static final long[] YELLOW_ZOBRIST_POSITIONS =
      createZobristPositions();
  private static HashMap<Long, Position> positions =
      new HashMap<Long, Position>();

  public static void clear() { positions.clear(); }

  private static long[] createZobristPositions() {
    Random random = new Random();
    long[] zobristPositions =
        new long[PositionConsts.HEIGHT * PositionConsts.WIDTH];

    for (int i = 0; i < zobristPositions.length; i++) {
      zobristPositions[i] = random.nextLong();
    }

    return zobristPositions;
  }

  public static long getMoveHash(long zobristHash, Turn turn, int moveColumn,
                                 int moveRow) {
    if (turn == Turn.RED) {
      return zobristHash ^
          RED_ZOBRIST_POSITIONS[moveColumn + moveRow * PositionConsts.WIDTH];
    } else {
      return zobristHash ^
          YELLOW_ZOBRIST_POSITIONS[moveColumn + moveRow * PositionConsts.WIDTH];
    }
  }

  public static Position getPosition(long newZobristHash) {
    return positions.get(newZobristHash);
  }

  public static void putPosition(long zobristHash, Position position) {
    positions.put(zobristHash, position);
  }
}
