package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Play {
	public Position findNextMove(Position position, Difficulty difficulty) {
		List<Position> positions = position.getPossibleMoves();
		Collections.shuffle(positions);
		
		for (int i = 1; i < difficulty.getDepth(); i++) {
			List<Position> newPositions = new ArrayList<Position>();
			for (int j : findBestMovesAtDepth(positions, i)) {
				newPositions.add(0, positions.remove(j));
			}
			newPositions.addAll(positions);
			positions = newPositions;
		}
		
		Position bestMove = positions.get(0);
		System.out.println(bestMove);
		return bestMove; 
	}

	private List<Integer> findBestMovesAtDepth(List<Position> positions, int depth) {
		ArrayList<Integer> bestMoves = new ArrayList<Integer>(); 
		int bestScore = -PositionConsts.WIN_SCORE;
		for (int i = 0; i<positions.size(); i++) {
			int moveScore = -negamax(positions.get(i), depth, -PositionConsts.WIN_SCORE, PositionConsts.WIN_SCORE);
			if (moveScore > bestScore) {
				bestMoves.clear();
				bestMoves.add(0, i);
				bestScore = moveScore;
			} else if (moveScore == bestScore) {
				bestMoves.add(0, i);
			}
		}
		return bestMoves;
	}

	private int negamax(Position position, int depth, int alpha, int beta) {
		if (position.isGameOver() || depth == 0) {
		    return position.score();
		} else {
			for (Position move : position.getPossibleMoves()) {
				alpha = Math.max(alpha, -negamax(move, depth-1, -beta, -alpha));
				if (alpha >= beta) {
					break;
				}
			}
			return alpha;
		}
	}

}
