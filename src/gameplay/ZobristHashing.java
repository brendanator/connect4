package gameplay;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Random;

public class ZobristHashing {
	public static final int ZOBRIST_SIZE = 64;

	private static final BitSet[] RED_ZOBRIST_POSITIONS = createZobristPositions();
	private static final BitSet[] YELLOW_ZOBRIST_POSITIONS = createZobristPositions();
	private static HashMap<BitSet, Position> positions = new HashMap<BitSet, Position>(); 

	public static void clear() {
		positions.clear();
	}
	
	private static BitSet[] createZobristPositions() {
		BitSet[] zobristPositions = new BitSet[PositionConsts.HEIGHT * PositionConsts.WIDTH];
		Random random = new Random();
		for (int i = 0; i < zobristPositions.length; i++) {
			BitSet bitSet = new BitSet(ZOBRIST_SIZE);
			for (int j = 0; j < bitSet.size(); j++) {
				bitSet.set(j, random.nextBoolean());
			}
			zobristPositions[i] = bitSet;
		}
		return zobristPositions;
	}

	public static BitSet getMoveHash(BitSet zobristHash, Turn turn,
			int positionIndex) {
		BitSet moveHash = (BitSet) zobristHash.clone();
		if (turn == Turn.RED) {
			moveHash.xor(RED_ZOBRIST_POSITIONS[positionIndex]);
		} else {
			moveHash.xor(YELLOW_ZOBRIST_POSITIONS[positionIndex]);
		}
		return moveHash;
	}
	
	public static Position getPosition(BitSet zobristHash) {
		return positions.get(zobristHash);
	}

	public static void putPosition(BitSet zobristHash, Position position) {
		positions.put(zobristHash, position);
	}
}
