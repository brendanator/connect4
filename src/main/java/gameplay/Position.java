package gameplay;

import static gameplay.PositionUtils.*;

import gui.BoardDisplay;
import java.util.ArrayList;

public class Position {
	private long zobristHash;
	private Turn turn;
	private long redPieces;
	private long yellowPieces;
	private int positionValue;
	
	public Position() {
		this(Turn.RED, 0, 0, 0);
	}

	private Position(Turn turn, long redPieces, long yellowPieces, long zobristHash) {
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
		positionValue = new PositionEvaluation(turn, redPieces, yellowPieces).getPositionValue();
	}

	public int score() {
		return turn == Turn.RED ? positionValue : -positionValue;
	}

	public boolean isGameOver() {
		return Math.abs(positionValue) == PositionConsts.WIN_SCORE || cardinality(redPieces + yellowPieces) == PositionConsts.SIZE;
	}

	public Position playMove(Integer column) {
		long pieces = redPieces | yellowPieces;
		int row = getNextEmptyRow(pieces, column);
		if (row < PositionConsts.HEIGHT) {
			return getNewPosition(column, row);
		} else {
			return null;
		}
	}

	public ArrayList<Position> getPossibleMoves() {
		ArrayList<Position> possibleMoves = new ArrayList<Position>();
		long pieces = redPieces | yellowPieces;
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
		newPosition = new Position(newTurn, newRedPieces, newYellowPieces, newZobristHash);
		ZobristHashing.putPosition(newZobristHash, newPosition);
		return newPosition;
	}

	private int getNextEmptyRow(long pieces, int column) {
		return cardinality(pieces & PositionConsts.COLUMNS[column]);
	}

	@Override
	public String toString() {
		return new PositionEvaluation(turn, redPieces, yellowPieces).toString();
	}

	public BoardDisplay getBoardDisplay() {
		return new PositionBoardDisplay(turn, redPieces, yellowPieces);
	}
}
