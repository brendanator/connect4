package gameplay;

import gui.BoardPosition;

import java.util.ArrayList;

public class Position implements BoardPosition {
	private long zobristHash;
	private long pieces;
	private long redPieces;
	private long yellowPieces;
	private Turn turn;
	private int positionValue;
	private long redWin = 0;
	private long yellowWin = 0;
	private DoubleThreat doubleThreat = DoubleThreat.NONE;
	private Zugwang zugwang = Zugwang.NONE;
	private long redThreats = 0;
	private long yellowThreats = 0;
	private int redMinorThreatCount = 0;
	private int yellowMinorThreatCount = 0;
	private int redBlockers = 0;
	private int yellowBlockers = 0;


	public Position() {
		this(0, 0, Turn.RED, 0);
	}

	public Position(long redPieces, long yellowPieces, Turn turn, long zobristHash) {
		this.pieces = redPieces | yellowPieces;
		this.redPieces = redPieces;
		this.yellowPieces = yellowPieces;
		this.turn = turn;
		this.zobristHash = zobristHash;
		evaluatePosition();
	}
	
	public long zobristHashCode() {
		return zobristHash;
	}

	private void evaluatePosition() {
		analyzePosition();

		if (redWin > 0) {
			positionValue =  PositionConsts.WIN_SCORE;
		} else if (yellowWin > 0) {
			positionValue =  -PositionConsts.WIN_SCORE;
		} else {
			positionValue =  0
			+ doubleThreat.score()
			+ zugwang.score()
			// TODO Only include horizontal and diagonal threats?
			+ cardinality(redThreats)*100 - cardinality(yellowThreats)*100
			// TODO Ignore this entirely? 
			+ redMinorThreatCount*10 - yellowMinorThreatCount*10 
			+ redBlockers - yellowBlockers;
		}
	}

	private void analyzePosition() {
		long redFourInARowChances = 0;
		long yellowFourInARowChances = 0;

		for (long fourInARow : PositionConsts.VERTICAL_FOURS_IN_A_ROW) {
			int redMatches = cardinality(redPieces & fourInARow);
			int yellowMatches = cardinality(yellowPieces & fourInARow);
			
			if (redMatches == 4) {
				redWin = fourInARow;
			} else if (yellowMatches == 4) {
				yellowWin = fourInARow;
			} else if (redMatches == 3 && yellowMatches == 0) {
				redFourInARowChances |= fourInARow;
			} else if (yellowMatches == 3 && redMatches == 0) {
				yellowFourInARowChances |= fourInARow;
			} 
		}

		for (int i = 0; i < PositionConsts.HORIZONTAL_ODD_FOURS_IN_A_ROW.length; i++) {
			long fourInARow = PositionConsts.HORIZONTAL_ODD_FOURS_IN_A_ROW[i];
			int row = (i % 4) * 2;

			int redMatches = cardinality(redPieces & fourInARow);
			int yellowMatches = cardinality(yellowPieces & fourInARow);
			
			if (redMatches == 4) {
				redWin = fourInARow;
			} else if (yellowMatches == 4) {
				yellowWin = fourInARow;
			} else if (redMatches == 3 && yellowMatches == 0) {
				redFourInARowChances |= fourInARow;
			} else if (yellowMatches == 3 && redMatches == 0) {
				yellowFourInARowChances |= fourInARow;
			} else if (redMatches == 2 && yellowMatches == 0) {
				redMinorThreatCount += 1;
			} else if (yellowMatches == 2 && redMatches == 0) {
				yellowMinorThreatCount += 1;
			} 

			if (redMatches >= 1) {
				redBlockers++;
			} 
			if (yellowMatches >= 1) {
				yellowBlockers += 8 - row;
			}
		}

		for (int i = 0; i < PositionConsts.HORIZONTAL_EVEN_FOURS_IN_A_ROW.length; i++) {
			long fourInARow = PositionConsts.HORIZONTAL_EVEN_FOURS_IN_A_ROW[i];
			int row = ((i % 4) * 2) + 1;
			
			int redMatches = cardinality(redPieces & fourInARow);
			int yellowMatches = cardinality(yellowPieces & fourInARow);
			
			if (redMatches == 4) {
				redWin = fourInARow;
			} else if (yellowMatches == 4) {
				yellowWin = fourInARow;
			} else if (redMatches == 3 && yellowMatches == 0) {
				redFourInARowChances |= fourInARow;
			} else if (yellowMatches == 3 && redMatches == 0) {
				yellowFourInARowChances |= fourInARow;
			} else if (redMatches == 2 && yellowMatches == 0) {
				redMinorThreatCount += 1;
			} else if (yellowMatches == 2 && redMatches == 0) {
				yellowMinorThreatCount += 1;
			} 

			if (redMatches >= 1) {
				redBlockers += 8 - row;
			} 
			if (yellowMatches >= 1) {
				yellowBlockers++;
			}
		}

		for (long fourInARow : PositionConsts.DIAGONAL_FOURS_IN_A_ROW) {
			int redMatches = cardinality(redPieces & fourInARow);
			int yellowMatches = cardinality(yellowPieces & fourInARow);
			
			if (redMatches == 4) {
				redWin = fourInARow;
			} else if (yellowMatches == 4) {
				yellowWin = fourInARow;
			} else if (redMatches == 3 && yellowMatches == 0) {
				redFourInARowChances |= fourInARow;
			} else if (yellowMatches == 3 && redMatches == 0) {
				yellowFourInARowChances |= fourInARow;
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
		
		redThreats = redFourInARowChances ^ (redPieces & redFourInARowChances);
		yellowThreats = yellowFourInARowChances ^ (yellowPieces & yellowFourInARowChances);

		analyzeThreats();
	}

	private int cardinality(long pieces) {
		return Long.bitCount(pieces);
	}

	// Opposing odds above evens and evens above odds can be ignored
	// Red odd un-opposed by yellow odd threat will win
	// Red even threats can be ignored
	// Repeating threats can be ignored e.g. Red odd above red odd = red odd
	// A red and yellow odd threat in same location can be treated as a red odd threat with a yellow odd threat above - FALSE
	private void analyzeThreats() {
		int sharedOddThreats = 0;
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
				long positionIndex = getPositionKey(i, j);
				boolean redThreat = (redThreats & positionIndex) > 0;
				boolean yellowThreat = (yellowThreats & positionIndex) > 0;
				boolean oddRow = j % 2 == 0;
				
				if (redThreat && yellowThreat) {
					if (lowestRedThreat == PositionConsts.HEIGHT && lowestYellowThreat == PositionConsts.HEIGHT) {
						if (oddRow) {
							sharedOddThreats += 1;
						} else {
							yellowEvenThreats = true;
						}
						break;
					}
				}
				
				if (redThreat) {
					lowestRedThreat = Math.min(lowestRedThreat, j);

					if (oddRow) {
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
						lowestRedDoubleThreat = Math.min(lowestRedDoubleThreat, (j-1-cardinality(pieces & PositionConsts.COLUMNS[i])));
					} else {
						existingRedThreat = true;
					}
				} else {
					existingRedThreat = false;
				}

				if (yellowThreat) {
					lowestYellowThreat = Math.min(lowestYellowThreat, j);
					
					if (oddRow) { // Odd threat
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
						lowestYellowDoubleThreat = Math.min(lowestYellowDoubleThreat, (j-1-cardinality(pieces & PositionConsts.COLUMNS[i])));
					} else {
						existingYellowThreat = true;
					}
				} else {
					existingYellowThreat = false;
				}

				if (redThreat && yellowThreat) {
					break;
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

		for (int i = 0; i < sharedOddThreats; i++) {
			if (i % 2 == 0) {
				redOddThreats += 1;
				yellowOddAboveRedOddThreats += 1;
			} else {
				redOddAboveYellowOddThreats += 1;
				yellowOddThreats += 1;				
			}
		}
		
		// TODO Fix when 2 yellow odd threats intersect two red odd threats
		if (!yellowEvenThreats &&
				(yellowOddThreats >= redOddThreats) &&
				(redOddThreats >= yellowOddThreats - 1) &&
				(redOddAboveYellowOddThreats == 0)) { 
			zugwang = Zugwang.NONE;
		} else if ((((redOddThreats > yellowOddThreats) || (yellowOddThreats == 1 && yellowOddAboveRedOddThreats == 0)) && 
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
		return Math.abs(positionValue) == PositionConsts.WIN_SCORE || cardinality(pieces) == PositionConsts.SIZE;
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
		long newZobristHash = ZobristHashing.getMoveHash(zobristHash, turn, moveColumn, moveRow);
		Position newPosition = ZobristHashing.getPosition(newZobristHash);
		if (newPosition != null)
			return newPosition;

		long newRedPieces = redPieces; 
		long newYellowPieces = yellowPieces;
		Turn newTurn;
		if (turn == Turn.RED) {
			newRedPieces |= getPositionKey(moveColumn, moveRow);
			newTurn = Turn.YELLOW;
		} else {
			newYellowPieces |= getPositionKey(moveColumn, moveRow);
			newTurn = Turn.RED;
		}
		newPosition = new Position(newRedPieces, newYellowPieces, newTurn, newZobristHash);
		ZobristHashing.putPosition(newZobristHash, newPosition);
		return newPosition;
	}

	private int getNextEmptyRow(long pieces, int column) {
		return cardinality(pieces & PositionConsts.COLUMNS[column]);
	}

	private long getPositionKey(int column, int row) {
		return (long) Math.pow(2, (column + row*PositionConsts.WIDTH));
	}

	@Override
	public String toString() {
		return String.format("Value: %d  DoubleThreat: %s  Zugwang: %s  Threats: %d, %d  Minor Threats: %d, %d  Blockers: %d %d",
				positionValue,
				doubleThreat,
				zugwang, 
				cardinality(redThreats), cardinality(yellowThreats),
				redMinorThreatCount, yellowMinorThreatCount,
				redBlockers, yellowBlockers);
	}

	@Override
	public boolean isRed(Integer column, Integer row) {
		return (redPieces & getPositionKey(column, row)) > 0;
	}

	@Override
	public boolean isYellow(Integer column, Integer row) {
		return (yellowPieces & getPositionKey(column, row)) > 0;
	}

	@Override
	public boolean isRedWin(Integer column, Integer row) {
		return (redWin & getPositionKey(column, row)) > 0;
	}

	@Override
	public boolean isYellowWin(Integer column, Integer row) {
		return (yellowWin & getPositionKey(column, row)) > 0;
	}

}
