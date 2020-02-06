package gameplay;

import static gameplay.PositionUtils.getPositionKey;

import gui.BoardDisplay;

public class PositionBoardDisplay implements BoardDisplay {
  private long redPieces;
  private long yellowPieces;
  private long redWin = 0;
  private long yellowWin = 0;

  public PositionBoardDisplay(Turn turn, long redPieces, long yellowPieces) {
    PositionEvaluation positionEvaluation =
        new PositionEvaluation(turn, redPieces, yellowPieces);
    this.redPieces = redPieces;
    this.yellowPieces = yellowPieces;
    this.redWin = positionEvaluation.getRedWin();
    this.yellowWin = positionEvaluation.getYellowWin();
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
