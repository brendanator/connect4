package gameplay;

import gui.BoardPosition;

import java.util.ArrayList;
import java.util.BitSet;

public class Position implements BoardPosition {
	private BitSet zobristHash;
	private BitSet pieces;
	private BitSet redPieces;
	private BitSet yellowPieces;
	private Turn turn;
	private int positionValue;
	private BitSet redWin = new BitSet(PositionConsts.SIZE);
	private BitSet yellowWin = new BitSet(PositionConsts.SIZE);
	private DoubleThreat doubleThreat = DoubleThreat.NONE;
	private Zugwang zugwang = Zugwang.NONE;
	private BitSet redThreats = new BitSet(PositionConsts.SIZE);
	private BitSet yellowThreats = new BitSet(PositionConsts.SIZE);
	private int redMinorThreatCount = 0;
	private int yellowMinorThreatCount = 0;
	private int redBlockers = 0;
	private int yellowBlockers = 0;


	public Position() {
		this(new BitSet(PositionConsts.SIZE), new BitSet(PositionConsts.SIZE), Turn.RED, new BitSet(ZobristHashing.ZOBRIST_SIZE));
	}

	public Position(BitSet redPieces, BitSet yellowPieces, Turn turn, BitSet zobristHash) {
		this.pieces = orBitSets(redPieces, yellowPieces);
		this.redPieces = redPieces;
		this.yellowPieces = yellowPieces;
		this.turn = turn;
		this.zobristHash = zobristHash;
		evaluatePosition();
	}

	private void evaluatePosition() {
		analyzePosition();

		if (redWin.cardinality() > 0) {
			positionValue =  PositionConsts.WIN_SCORE;
		} else if (yellowWin.cardinality() > 0) {
			positionValue =  -PositionConsts.WIN_SCORE;
		} else {
			positionValue =  0
			+ doubleThreat.score()
			+ zugwang.score()
			+ redThreats.cardinality()*100 - yellowThreats.cardinality()*100
			// TODO Only include horizontal and diagonal threats?
			+ redMinorThreatCount*10 - yellowMinorThreatCount*10 
			+ redBlockers - yellowBlockers;
		}
	}

	private void analyzePosition() {
		BitSet redFourInARowChances = new BitSet(PositionConsts.SIZE);
		BitSet yellowFourInARowChances = new BitSet(PositionConsts.SIZE);

		for (BitSet fourInARow : PositionConsts.FOURS_IN_A_ROW) {
			int redMatches = andBitSets(redPieces, fourInARow).cardinality();
			int yellowMatches = andBitSets(yellowPieces, fourInARow).cardinality();
			if (redMatches == 4) {
				redWin = fourInARow;
			} else if (yellowMatches == 4) {
				yellowWin = fourInARow;
			} else if (redMatches == 3 && yellowMatches == 0) {
				redFourInARowChances.or(fourInARow);
			} else if (yellowMatches == 3 && redMatches == 0) {
				yellowFourInARowChances.or(fourInARow);
			} else if (redMatches == 2 && yellowMatches == 0) {
				redMinorThreatCount += 1;
			} else if (yellowMatches == 2 && redMatches == 0) {
				yellowMinorThreatCount += 1;
			} 

			if (redMatches >= 1) {
				redBlockers++;
			} 
			if (yellowMatches >= 1) {
				yellowBlockers++;
			}
		}

		redThreats = xorBitSets(redFourInARowChances, andBitSets(redPieces, redFourInARowChances));
		yellowThreats = xorBitSets(yellowFourInARowChances, andBitSets(yellowPieces, yellowFourInARowChances));

		analyzeThreats();
	}

	// Opposing odds above evens and evens above odds can be ignored
	// Red odd un-opposed by yellow odd threat will win
	// Red even threats can be ignored
	// Repeating threats can be ignored e.g. Red odd above red odd = red odd
	// A red and yellow odd threat in same location can be treated as a red odd threat with a yellow odd threat above
	private void analyzeThreats() {
		int redOddThreats = 0;
		int yellowOddThreats = 0;
		int redOddAboveYellowOddThreats = 0;
		int yellowOddAboveRedOddThreats = 0;
		boolean yellowEvenThreats = false;
		int lowestRedDoubleThreat = PositionConsts.HEIGHT;
		int lowestYellowDoubleThreat = PositionConsts.HEIGHT;

		for(int i = 0; i < PositionConsts.WIDTH; i++) {
			int lowestRedThreat = PositionConsts.HEIGHT;
			boolean redOddThreat = false;
			boolean redEvenThreat = false;
			int lowestYellowThreat = PositionConsts.HEIGHT;
			boolean yellowOddThreat = false;
			boolean yellowEvenThreat = false;

			boolean existingRedThreat = false;
			boolean existingYellowThreat = false;

			for (int j = 0; j < PositionConsts.HEIGHT; j++) {
				int positionIndex = getPositionIndex(i, j);

				if (redThreats.get(positionIndex)) {
					lowestRedThreat = Math.min(lowestRedThreat, j);

					if (j % 2 == 0) { // Odd threat
						if (!redOddThreat && !yellowEvenThreat) {
							redOddThreat = true;
							if (!yellowOddThreat) {
								redOddThreats += 1;
							} else {
								redOddAboveYellowOddThreats += 1;
							}
						}
					} else { // Even threat
						redEvenThreat = true;
					}

					if (existingRedThreat && lowestYellowThreat >= j) {
						lowestRedDoubleThreat = Math.min(lowestRedDoubleThreat, (j-1-andBitSets(pieces, PositionConsts.COLUMNS[i]).cardinality()));
					} else {
						existingRedThreat = true;
					}
				} else {
					existingRedThreat = false;
				}


				if (yellowThreats.get(positionIndex)) {
					lowestYellowThreat = Math.min(lowestYellowThreat, j);
					
					if (j % 2 == 0) { // Odd threat
						if (!yellowOddThreat && !redEvenThreat) {
							yellowOddThreat = true;
							if (!redOddThreat) {
								yellowOddThreats += 1;
							} else {
								yellowOddAboveRedOddThreats += 1;
							}
						}
					} else { // Even threat
							yellowEvenThreats = true;
							yellowEvenThreat = true;
					}
					
					if (existingYellowThreat && lowestRedThreat >= j) {
						lowestYellowDoubleThreat = Math.min(lowestYellowDoubleThreat, (j-1-andBitSets(pieces, PositionConsts.COLUMNS[i]).cardinality()));
					} else {
						existingYellowThreat = true;
					}
				} else {
					existingYellowThreat = false;
				}
			}
		}
		
		if (lowestRedDoubleThreat == PositionConsts.HEIGHT && lowestYellowDoubleThreat == PositionConsts.HEIGHT )
			doubleThreat = DoubleThreat.NONE;
		else if (turn == Turn.RED) {
			if (lowestRedDoubleThreat <= lowestYellowDoubleThreat) {
				doubleThreat = DoubleThreat.RED;
			} else {
				doubleThreat = DoubleThreat.YELLOW;
			}
		} else {
			if (lowestYellowDoubleThreat <= lowestRedDoubleThreat) {
				doubleThreat = DoubleThreat.YELLOW;
			} else {
				doubleThreat = DoubleThreat.RED;
			}
		}

		// TODO Fix when 2 yellow odd threats intersect two red odd threats
		if (!yellowEvenThreats &&
				(yellowOddThreats >= redOddThreats) &&
				(redOddThreats >= yellowOddThreats - 1) &&
				(redOddAboveYellowOddThreats == 0)) { 
			zugwang = Zugwang.NONE;
		} else if (((redOddThreats > yellowOddThreats) && 
				(redOddThreats + redOddAboveYellowOddThreats >= yellowOddThreats + yellowOddAboveRedOddThreats))
				|| (redOddThreats + redOddAboveYellowOddThreats > yellowOddThreats + yellowOddAboveRedOddThreats)) {
			zugwang = Zugwang.RED;
		} else {
			zugwang = Zugwang.YELLOW;
		}
	}

	public int score() {
		return turn == Turn.RED ? positionValue : -positionValue;
	}

	public boolean isGameOver() {
		return Math.abs(positionValue) == PositionConsts.WIN_SCORE || pieces.cardinality() == PositionConsts.SIZE;
	}

	public Position playMove(Integer column) {
		int row = getNextEmptyRow(pieces, column);
		if (row < PositionConsts.HEIGHT) {
			return getNewPosition(column, row);
		} else {
			return null;
		}
	}

	public ArrayList<Position> getPossibleMoves() {
		ArrayList<Position> possibleMoves = new ArrayList<Position>();
		for (int column : PositionConsts.columnIndices) {
			int row = getNextEmptyRow(pieces, column);
			if (row < PositionConsts.HEIGHT) {
				possibleMoves.add(getNewPosition(column, row));
			}
		}
		return possibleMoves;
	}

	private Position getNewPosition(int moveColumn, int moveRow) {
		int positionIndex = getPositionIndex(moveColumn, moveRow);
		BitSet newZobristHash = ZobristHashing.getMoveHash(zobristHash, turn, positionIndex);
		Position newPosition = ZobristHashing.getPosition(newZobristHash);
		if (newPosition != null)
			return newPosition;

		BitSet newRedPieces = (BitSet) redPieces.clone();
		BitSet newYellowPieces = (BitSet) yellowPieces.clone();
		Turn newTurn;
		if (turn == Turn.RED) {
			newRedPieces.set(positionIndex);
			newTurn = Turn.YELLOW;
		} else {
			newYellowPieces.set(positionIndex);
			newTurn = Turn.RED;
		}
		newPosition = new Position(newRedPieces, newYellowPieces, newTurn, newZobristHash);
		ZobristHashing.putPosition(newZobristHash, newPosition);
		return newPosition;
	}

	private int getNextEmptyRow(BitSet pieces, int column) {
		return andBitSets(pieces, PositionConsts.COLUMNS[column]).cardinality();
	}

	private int getPositionIndex(int column, int row) {
		return column + row*PositionConsts.WIDTH;
	}

	private BitSet andBitSets(BitSet bitSet1, BitSet bitSet2) {
		BitSet result = (BitSet) bitSet1.clone();
		result.and(bitSet2);
		return result;
	}

	private BitSet orBitSets(BitSet bitSet1, BitSet bitSet2) {
		BitSet result = (BitSet) bitSet1.clone();
		result.or(bitSet2);
		return result;
	}

	private BitSet xorBitSets(BitSet bitSet1, BitSet bitSet2) {
		BitSet result = (BitSet) bitSet1.clone();
		result.xor(bitSet2);
		return result;
	}

	@Override
	public String toString() {
		return String.format("Value: %d  DoubleThreat: %s  Zugwang: %s  Threats: %d, %d  Minor Threats: %d, %d  Blockers: %d %d",
				positionValue,
				doubleThreat,
				zugwang, 
				redThreats.cardinality(), yellowThreats.cardinality(),
				redMinorThreatCount, yellowMinorThreatCount,
				redBlockers, yellowBlockers);
	}

	@Override
	public boolean isRed(Integer column, Integer row) {
		return redPieces.get(getPositionIndex(column, row));
	}

	@Override
	public boolean isYellow(Integer column, Integer row) {
		return yellowPieces.get(getPositionIndex(column, row));
	}

	@Override
	public boolean isRedWin(Integer column, Integer row) {
		return redWin.get(getPositionIndex(column, row));
	}

	@Override
	public boolean isYellowWin(Integer column, Integer row) {
		return yellowWin.get(getPositionIndex(column, row));
	}

}
